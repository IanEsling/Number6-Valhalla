package dev.number6.slack;

import dev.number6.slack.port.HttpPort;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class FakeHttpModule {

    @Provides
    @Singleton
    public HttpPort providesHttp() {
        return new FakeHttpAdaptor();
    }
}
