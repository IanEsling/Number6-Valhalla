package dev.number6.slackreader.mn;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.time.Clock;

@Factory
public class SnsFactory {

    @Singleton
    AmazonSNS sns() {
        return AmazonSNSClientBuilder.defaultClient();
    }

    @Singleton
    Clock clock(){
        return Clock.systemDefaultZone();
    }
}
