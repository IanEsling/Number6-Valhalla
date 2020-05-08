package dev.number6.slack;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Arrays;

class FakeLogger implements LambdaLogger {

    @Override
    public void log(String message) {
        System.out.println("Logging!");
        System.out.println(message);
    }

    @Override
    public void log(byte[] message) {
        System.out.println("whut?");
        System.out.println(Arrays.toString(message));
    }
}
