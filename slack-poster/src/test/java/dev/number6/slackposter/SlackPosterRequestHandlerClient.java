package dev.number6.slackposter;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;

import javax.inject.Named;

@FunctionClient
public interface SlackPosterRequestHandlerClient {

    Single<String> apply(@Body DynamodbEvent event);

}
