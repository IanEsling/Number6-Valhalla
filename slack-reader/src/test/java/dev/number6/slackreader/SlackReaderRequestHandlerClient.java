package dev.number6.slackreader;

import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;

import javax.inject.Named;
import java.util.Map;

@FunctionClient
public interface SlackReaderRequestHandlerClient {

    @Named("slack-reader")
    Single<String> apply(@Body Map<String, Object> event);

}
