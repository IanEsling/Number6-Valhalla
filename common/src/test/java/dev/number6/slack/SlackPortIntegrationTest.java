package dev.number6.slack;

import dev.number6.slack.adaptor.SlackClientAdaptor;
import dev.number6.slack.port.HttpPort;
import dev.number6.slack.port.SlackPort;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class SlackPortIntegrationTest {

    @Inject
    SlackPort slackPort;

    @MockBean(HttpPort.class)
    HttpPort httpPort() {
        return new FakeHttpAdaptor();
    }

    @Test
    void get() {
        Optional<String> response = slackPort.getSlackResponse(SlackClientAdaptor.CHANNEL_HISTORY_URL, String.class, new FakeLogger());
        assertThat(response.isPresent()).isTrue();
    }

    @Test
    void postNoResponse() {
        Optional<String> response = slackPort.getSlackResponse(SlackClientAdaptor.CHANNEL_HISTORY_URL, new FakeLogger(), "body");
        assertThat(response.isPresent()).isFalse();
    }

    @Test
    void postWithResponse() {
        Optional<String> response = slackPort.getSlackResponse(SlackClientAdaptor.CHANNEL_HISTORY_URL, "body", String.class, new FakeLogger());
        assertThat(response.isPresent()).isTrue();
        assertThat(response.get()).isEqualTo("response");
    }

}
