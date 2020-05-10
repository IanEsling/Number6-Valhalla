package dev.number6.slack;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dev.number6.slack.port.HttpPort;

public class FakeHttpAdaptor implements HttpPort {

    private String response = "response";

    @Override
    public CallResponse get(String url, LambdaLogger logger) {
        return new CallResponse(response);
    }

    @Override
    public CallResponse post(String url, String body, LambdaLogger logger) {
        return new CallResponse(response);
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
