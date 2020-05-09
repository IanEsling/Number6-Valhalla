package dev.number6.slackreader;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class SlackReaderIntegrationTest {

    @Inject
    SlackReader reader;

    @Test
    void wiring() {
        assertThat(reader).isNotNull();
    }
}
