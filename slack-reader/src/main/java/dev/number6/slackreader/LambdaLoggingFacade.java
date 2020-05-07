package dev.number6.slackreader;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.slf4j.Logger;

public class LambdaLoggingFacade implements LambdaLogger {
    private final Logger logger;

    public LambdaLoggingFacade(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(String message) {
        logger.debug(message);
    }

    @Override
    public void log(byte[] message) {
        throw new UnsupportedOperationException("say whut now?");
    }
}
