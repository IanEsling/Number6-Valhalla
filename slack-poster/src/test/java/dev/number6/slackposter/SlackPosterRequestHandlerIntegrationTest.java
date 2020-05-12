package dev.number6.slackposter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import dev.number6.slackposter.model.PresentableChannelSummary;
import dev.number6.slackposter.port.SlackPort;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MicronautTest
public class SlackPosterRequestHandlerIntegrationTest {

    private final SlackPort mockSlack = mock(SlackPort.class);
    @Inject
    SlackPosterRequestHandlerClient client;

    @MockBean(SlackPort.class)
    SlackPort slack() {
        return mockSlack;
    }

    @Test
    void wiring() {
        DynamodbEvent event = new DynamodbEvent();
        DynamodbEvent.DynamodbStreamRecord record = new DynamodbEvent.DynamodbStreamRecord();
        record.setEventName("MODIFY");
        StreamRecord streamRecord = new StreamRecord().withNewImage(Map.of(ChannelSummaryImage.VERSION_KEY, new AttributeValue().withN(String.valueOf(ChannelSummaryImage.FINAL_UPDATE_MINIMUM_VERSION))));
        record.setDynamodb(streamRecord);
        event.setRecords(List.of(record));

        String result = client.apply(event).blockingGet();
        assertThat(result).isEqualTo("ok");
        verify(mockSlack).postMessageToChannel(any(), any());
    }
}
