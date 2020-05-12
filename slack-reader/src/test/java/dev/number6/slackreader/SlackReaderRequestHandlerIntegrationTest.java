package dev.number6.slackreader;

import dev.number6.db.port.BasicDatabasePort;
import dev.number6.message.ChannelMessages;
import dev.number6.slackreader.generate.SlackReaderRDG;
import dev.number6.slackreader.model.Channel;
import dev.number6.slackreader.model.Message;
import dev.number6.slackreader.port.NotificationPort;
import dev.number6.slackreader.port.SlackPort;
import dev.number6.slackreader.port.SlackReaderConfigurationPort;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.org.fyodor.range.Range;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MicronautTest
class SlackReaderRequestHandlerIntegrationTest {

    private final NotificationPort mockSns = mock(NotificationPort.class);
    private final SlackReaderConfigurationPort mockConfig = mock(SlackReaderConfigurationPort.class);
    private final SlackPort mockSlack = mock(SlackPort.class);
    private final BasicDatabasePort mockDb = mock(BasicDatabasePort.class);

    @Inject
    SlackReaderRequestHandlerClient client;

    @MockBean(NotificationPort.class)
    NotificationPort sns() {
        return mockSns;
    }

    @MockBean(BasicDatabasePort.class)
    BasicDatabasePort db() {
        return mockDb;
    }

    @MockBean(SlackReaderConfigurationPort.class)
    SlackReaderConfigurationPort config() {
        return mockConfig;
    }

    @MockBean(SlackPort.class)
    SlackPort http() {
        return mockSlack;
    }

    @Test
    void wiring() {
        Map<Channel, Collection<Message>> channelMessages = SlackReaderRDG.channels(Range.closed(5, 15)).next()
                .stream()
                .collect(Collectors.toMap(Function.identity(), c -> SlackReaderRDG.list(SlackReaderRDG.message()).next()));

        when(mockSlack.getChannelList(any())).thenReturn(channelMessages.keySet());
        when(mockSlack.getMessagesForChannelOnDate(any(), any(), any())).thenAnswer(invocationOnMock -> channelMessages.get(invocationOnMock.getArgument(0)));

        Map<String, Object> event = Map.of();
        String result = client.apply(event).blockingGet();

        ArgumentCaptor<ChannelMessages> channelMessagesCaptor = ArgumentCaptor.forClass(ChannelMessages.class);
        ArgumentCaptor<Collection<String>> channels = ArgumentCaptor.forClass(Collection.class);
        verify(mockSns, times(channelMessages.size())).broadcast(channelMessagesCaptor.capture());
        verify(mockDb, times(1)).createNewSummaryForChannels(channels.capture(), any());
        assertThat(result).isEqualTo("ok");
        assertThat(channels.getValue()).containsExactlyInAnyOrderElementsOf(channelMessages.keySet().stream().map(Channel::getName).collect(Collectors.toList()));
        channelMessagesCaptor.getAllValues().forEach(cm -> {
            assertThat(cm.getChannelName()).isIn(channelMessages.keySet().stream().map(Channel::getName).collect(Collectors.toList()));
            assertThat(cm.getMessages()).containsExactlyInAnyOrderElementsOf(channelMessages.entrySet()
                    .stream().filter(e -> e.getKey().getName().equalsIgnoreCase(cm.getChannelName()))
                    .findFirst().orElseThrow(() -> new RuntimeException("channel name not found : " + cm.getChannelName()))
                    .getValue().stream().map(Message::getText).collect(Collectors.toList()));
        });
    }
}
