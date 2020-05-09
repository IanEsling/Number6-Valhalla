package dev.number6.comprehend;

import dev.number6.comprehend.adaptor.AwsComprehensionAdaptor;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class AwsComprehensionAdaptorIntegrationTest {

    @Inject
    AwsComprehensionAdaptor testee;

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}
