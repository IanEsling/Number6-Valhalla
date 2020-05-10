package dev.number6.slack;

import dev.number6.slack.port.SlackPort;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class SlackPortIntegrationTest {

    @Inject
    SlackPort slackPort;

    @Test
    void wiring() {
        assertThat(slackPort).isNotNull();
    }
}
