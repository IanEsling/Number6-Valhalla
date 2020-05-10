package dev.number6.slack.mn;

import io.micronaut.context.annotation.Factory;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import javax.inject.Singleton;

@Factory
public class HttpClientFactory {

    @Singleton
    public Call.Factory getCallFactory() {
        return new OkHttpClient();
    }
}
