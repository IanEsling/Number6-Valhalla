package dev.number6.keyphrases;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;

import javax.inject.Named;

@FunctionClient
public interface ChannelMessageKeyPhrasesComprehensionClient {

    @Named("channel-messages")
    Single<String> apply(@Body SNSEvent event);

}
