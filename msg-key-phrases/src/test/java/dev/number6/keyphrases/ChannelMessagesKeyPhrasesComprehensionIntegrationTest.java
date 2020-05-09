package dev.number6.keyphrases;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.db.port.DatabasePort;
import dev.number6.message.ChannelMessages;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@MicronautTest
class ChannelMessagesKeyPhrasesComprehensionIntegrationTest {


    private final ComprehensionPort mockComprehend = mock(ComprehensionPort.class);
    private final DatabasePort mockDatabase = mock(DatabasePort.class);
    Gson gson = new Gson();
    ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();
    @Inject
    ChannelMessagesNotificationRequestHandler testee;

    @MockBean(ComprehensionPort.class)
    ComprehensionPort comprehendClient() {
        return mockComprehend;
    }

    @MockBean(DatabasePort.class)
    DatabasePort dbMapper() {
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

        PresentableKeyPhrasesResults entityResults = new PresentableKeyPhrasesResultsGenerator().next();
        when(mockComprehend.getKeyPhrasesForSlackMessages(messages)).thenReturn(entityResults);

        Context mockContext = mock(Context.class);
        when(mockContext.getLogger()).thenReturn(mock(LambdaLogger.class));
        testee.apply(event);

        verify(mockDatabase).save(entityResults);
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