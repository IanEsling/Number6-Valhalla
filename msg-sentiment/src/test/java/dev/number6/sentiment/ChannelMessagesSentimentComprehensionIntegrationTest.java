package dev.number6.sentiment;

import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.amazonaws.services.comprehend.model.SentimentScore;
import com.amazonaws.services.comprehend.model.SentimentType;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableSentimentResults;
import dev.number6.db.port.FullDatabasePort;
import dev.number6.message.ChannelMessages;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
class ChannelMessagesSentimentComprehensionIntegrationTest {

    private final ComprehensionPort mockComprehend = mock(ComprehensionPort.class);
    private final FullDatabasePort mockDatabase = mock(FullDatabasePort.class);
    Gson gson = new Gson();
    ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();
    @Inject
    ChannelMessageSentimentComprehensionClient testee;

    public static float sentimentScoreFloat() {
        return RDG.doubleVal(1d).next().floatValue();
    }

    @MockBean(ComprehensionPort.class)
    ComprehensionPort comprehendClient() {
        return mockComprehend;
    }

    @MockBean(FullDatabasePort.class)
    FullDatabasePort dbMapper() {
        return mockDatabase;
    }

    @Test
    void providesChannelHandler() {
        ChannelMessages messages = channelMessagesGenerator.next();
        SNSEvent.SNS sns = new SNSEvent.SNS();
        sns.setMessage(gson.toJson(messages));
        SNSEvent.SNSRecord record = new SNSEvent.SNSRecord();
        record.setSns(sns);
        SNSEvent event = new SNSEvent();
        event.setRecords(List.of(record));

        PresentableSentimentResults sentimentResults = new PresentableSentimentResultsGenerator().next();
        when(mockComprehend.getSentimentForSlackMessages(messages)).thenReturn(sentimentResults);

        String result = testee.apply(event).blockingGet();

        assertThat(result).isEqualTo("ok");
        verify(mockDatabase).save(sentimentResults);
    }

    public static class ChannelMessagesGenerator implements Generator<ChannelMessages> {

        @Override
        public ChannelMessages next() {
            return new ChannelMessages(RDG.string().next(), RDG.list(RDG.string()).next(), LocalDate.now());
        }
    }


    public static class PresentableSentimentResultsGenerator implements Generator<PresentableSentimentResults> {

        Generator<List<DetectSentimentResult>> detectSentimentResultsGenerator = RDG.list(new DetectSentimentResultGenerator(), Range.closed(10, 20));

        @Override
        public PresentableSentimentResults next() {
            return new PresentableSentimentResults(LocalDate.now(),
                    detectSentimentResultsGenerator.next(),
                    RDG.string(20).next());
        }

    }

    public static class DetectSentimentResultGenerator implements Generator<DetectSentimentResult> {

        SentimentScoreGenerator sentimentScoreGenerator = new SentimentScoreGenerator();
        Generator<SentimentType> sentimentTypeGenerator = RDG.value(SentimentType.class);

        @Override
        public DetectSentimentResult next() {
            return new DetectSentimentResult()
                    .withSentiment(sentimentTypeGenerator.next())
                    .withSentimentScore(sentimentScoreGenerator.next());
        }

    }

    public static class SentimentScoreGenerator implements Generator<SentimentScore> {

        @Override
        public SentimentScore next() {
            return new SentimentScore()
                    .withMixed(sentimentScoreFloat())
                    .withNegative(sentimentScoreFloat())
                    .withPositive(sentimentScoreFloat())
                    .withNeutral(sentimentScoreFloat());
        }
    }
}