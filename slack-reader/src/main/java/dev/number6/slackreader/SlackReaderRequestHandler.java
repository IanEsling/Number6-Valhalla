package dev.number6.slackreader;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dev.number6.slack.LambdaLoggingFacade;
import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

@FunctionBean
public class SlackReaderRequestHandler extends FunctionInitializer implements Function<Map<String, Object>, String> {

    private static final Logger LOG = LoggerFactory.getLogger(SlackReaderRequestHandler.class);
    private final LambdaLogger logger = new LambdaLoggingFacade(LOG);
    @Inject
    SlackReader handler;

    @Override
    public String apply(Map<String, Object> o) {
        handler.handle(o, logger);
        return "ok";
    }
}
