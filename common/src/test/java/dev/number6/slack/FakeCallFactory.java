package dev.number6.slack;

import okhttp3.*;
import org.apache.http.entity.ContentType;

public class FakeCallFactory implements Call.Factory {

    private final String response;

    public FakeCallFactory(String response) {
        this.response = response;
    }

    @Override
    public Call newCall(Request request) {
        return new Call() {

            @Override
            public Request request() {
                throw new UnsupportedOperationException("request not supported in fake Call");
            }

            @Override
            public Response execute() {
                ResponseBody responseBody = ResponseBody.create(MediaType.parse(ContentType.APPLICATION_JSON.toString()),
                        response);
                return new Response.Builder()
                        .protocol(Protocol.HTTP_1_1)
                        .request(new Request.Builder().url("http://localhost:8080").build())
                        .code(200)
                        .message("message")
                        .body(responseBody)
                        .build();
            }

            @Override
            public void enqueue(Callback responseCallback) {
                throw new UnsupportedOperationException("enqueue not supported in fake Call");
            }

            @Override
            public void cancel() {
                throw new UnsupportedOperationException("cancel not supported in fake Call");
            }

            @Override
            public boolean isExecuted() {
                throw new UnsupportedOperationException("isExecuted not supported in fake Call");
            }

            @Override
            public boolean isCanceled() {
                throw new UnsupportedOperationException("isCanceled not supported in fake Call");
            }

            @Override
            public Call clone() {
                throw new UnsupportedOperationException("clone not supported in fake Call");
            }
        };
    }
}
