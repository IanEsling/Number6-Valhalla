package dev.number6.comprehend;

import com.amazonaws.services.comprehend.AmazonComprehend;
import dev.number6.comprehend.adaptor.AwsComprehensionAdaptor;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MicronautTest
public class AwsComprehensionAdaptorIntegrationTest {

    AmazonComprehend mockAmazonComprehend = mock(AmazonComprehend.class);

    @MockBean(AmazonComprehend.class)
    AmazonComprehend client(){
        return mockAmazonComprehend;
    }

    @Inject
    AwsComprehensionAdaptor testee;

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}
