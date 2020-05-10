package dev.number6.db;

import dev.number6.db.port.FullDatabasePort;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class FullDatabasePortIntegrationTest {

    @Inject
    FullDatabasePort testee;

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}