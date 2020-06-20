package dev.number6.entity;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;

import javax.inject.Named;

@FunctionClient
public interface ChannelMessageEntityComprehensionClient {

    Single<String> apply(@Body SNSEvent event);

}
