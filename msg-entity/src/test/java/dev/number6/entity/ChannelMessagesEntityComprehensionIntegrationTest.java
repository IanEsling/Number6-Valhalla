package dev.number6.entity;

import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import dev.number6.comprehend.adaptor.AwsComprehensionAdaptor;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.comprehend.results.PresentableSentimentResults;
import dev.number6.db.port.FullDatabasePort;
import dev.number6.message.ChannelMessages;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
class ChannelMessagesEntityComprehensionIntegrationTest {

    Gson gson = new Gson();
    ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();

    AmazonComprehend mockAmazonComprehend = mock(AmazonComprehend.class);

    private final ComprehensionPort mockComprehend = mock(ComprehensionPort.class);
    private final FullDatabasePort mockDatabase = mock(FullDatabasePort.class);

    @MockBean(AmazonComprehend.class)
    AmazonComprehend client() {
        return mockAmazonComprehend;
    }

    @Replaces(AwsComprehensionAdaptor.class)
    @MockBean(AwsComprehensionAdaptor.class)
    @Requires(env = Environment.TEST)
    ComprehensionPort comprehendClient() {
        return mockComprehend;
    }

    @MockBean(FullDatabasePort.class)
    FullDatabasePort dbMapper() {
        return mockDatabase;
    }

    @Inject
    ChannelMessageEntityComprehensionClient testee;

    @Test
    void providesChannelHandler() {
        ChannelMessages messages = channelMessagesGenerator.next();
        SNSEvent.SNS sns = new SNSEvent.SNS();
        sns.setMessage(gson.toJson(messages));
        SNSEvent.SNSRecord record = new SNSEvent.SNSRecord();
        record.setSns(sns);
        SNSEvent event = new SNSEvent();
        event.setRecords(List.of(record));

        PresentableEntityResults entityResults = new PresentableEntityResultsGenerator().next();
        when(mockComprehend.getEntitiesForSlackMessages(messages)).thenReturn(entityResults);

        Context mockContext = mock(Context.class);
        when(mockContext.getLogger()).thenReturn(mock(LambdaLogger.class));
        String result = testee.apply(event).blockingGet();

        assertThat(result).isEqualTo("ok");
        verify(mockDatabase).save(entityResults);
    }

    public static class ChannelMessagesGenerator implements Generator<ChannelMessages> {

        @Override
        public ChannelMessages next() {
            return new ChannelMessages(RDG.string().next(), RDG.list(RDG.string()).next(), LocalDate.now());
        }
    }

    public static class PresentableEntityResultsGenerator implements Generator<PresentableEntityResults> {

        Generator<Map<String, Map<String, Long>>> entityResultsGenerator = RDG.map(RDG.string(10),
                RDG.map(RDG.string(10), RDG.longVal(100)));

        @Override
        public PresentableEntityResults next() {
            return new PresentableEntityResults(LocalDate.now(), entityResultsGenerator.next(), RDG.string().next());
        }
    }
}