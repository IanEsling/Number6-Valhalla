package dev.number6.slackreader.dagger

import dagger.Module
import dagger.Provides
import dev.number6.slack.port.HttpPort
import javax.inject.Singleton

@Module
class FakeHttpModule {
    @Provides
    @Singleton
    fun providesHttp(): HttpPort {
        return FakeHttpAdaptor()
    }
}