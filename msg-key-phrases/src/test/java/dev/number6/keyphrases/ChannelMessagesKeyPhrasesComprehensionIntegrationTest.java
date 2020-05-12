package dev.number6.keyphrases;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.db.port.FullDatabasePort;
import dev.number6.message.ChannelMessages;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
class ChannelMessagesKeyPhrasesComprehensionIntegrationTest {

    private final ComprehensionPort mockComprehend = mock(ComprehensionPort.class);
    private final FullDatabasePort mockDatabase = mock(FullDatabasePort.class);
    Gson gson = new Gson();
    ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();
    @Inject
    ChannelMessageKeyPhrasesComprehensionClient testee;

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

        PresentableKeyPhrasesResults keyPhrasesResults = new PresentableKeyPhrasesResultsGenerator().next();
        when(mockComprehend.getKeyPhrasesForSlackMessages(messages)).thenReturn(keyPhrasesResults);

        String result = testee.apply(event).blockingGet();

        assertThat(result).isEqualTo("ok");
        verify(mockDatabase).save(keyPhrasesResults);
    }

    public static class ChannelMessagesGenerator implements Generator<ChannelMessages> {

        @Override
        public ChannelMessages next() {
            return new ChannelMessages(RDG.string().next(), RDG.list(RDG.string()).next(), LocalDate.now());
        }
    }

    public static class PresentableKeyPhrasesResultsGenerator implements Generator<PresentableKeyPhrasesResults> {

        Generator<Map<String, Long>> keyPhrasesResultsGenerator = RDG.map(RDG.string(10),
                RDG.longVal(100));

        @Override
        public PresentableKeyPhrasesResults next() {
            return new PresentableKeyPhrasesResults(LocalDate.now(), keyPhrasesResultsGenerator.next(), RDG.string().next());
        }
    }
}