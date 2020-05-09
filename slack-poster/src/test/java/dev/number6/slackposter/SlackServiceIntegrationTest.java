package dev.number6.slackposter;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class SlackServiceIntegrationTest {

    @Inject
    SlackService testee;

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}
