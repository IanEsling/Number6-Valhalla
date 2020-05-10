package dev.number6.entity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.db.port.DatabasePort;
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

import static org.mockito.Mockito.*;

@MicronautTest
class ChannelMessagesEntityComprehensionIntegrationTest {

    private final ComprehensionPort mockComprehend = mock(ComprehensionPort.class);
    private final DatabasePort mockDatabase = mock(DatabasePort.class);
    Gson gson = new Gson();
    ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();
    @Inject
    ChannelMessagesRequestHandler testee;

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

        PresentableEntityResults entityResults = new PresentableEntityResultsGenerator().next();
        when(mockComprehend.getEntitiesForSlackMessages(messages)).thenReturn(entityResults);

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

    public static class PresentableEntityResultsGenerator implements Generator<PresentableEntityResults> {

        Generator<Map<String, Map<String, Long>>> entityResultsGenerator = RDG.map(RDG.string(10),
                RDG.map(RDG.string(10), RDG.longVal(100)));

        @Override
        public PresentableEntityResults next() {
            return new PresentableEntityResults(LocalDate.now(), entityResultsGenerator.next(), RDG.string().next());
        }
    }
}