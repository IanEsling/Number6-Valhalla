package dev.number6.comprehend.mn;

import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class ComprehendFactory {

    @Singleton
    AmazonComprehend amazonComprehend() {
        return AmazonComprehendClientBuilder.defaultClient();
    }
}
