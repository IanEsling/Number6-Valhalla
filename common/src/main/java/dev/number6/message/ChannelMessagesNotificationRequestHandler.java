package dev.number6.message;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import io.micronaut.function.FunctionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

@FunctionBean("channel-messages")
public class ChannelMessagesNotificationRequestHandler implements Function<SNSEvent, String> {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelMessagesNotificationRequestHandler.class);
    private final Gson gson = new Gson();
    private final ChannelMessagesHandler channelMessagesHandler;

    public ChannelMessagesNotificationRequestHandler(ChannelMessagesHandler channelMessagesHandler) {
        this.channelMessagesHandler = channelMessagesHandler;
    }

//    @Override
//    public String handleRequest(SNSEvent event, Context context) {
//        LambdaLogger logger = context.getLogger();
//
//        logger.log("Starting SnsMessage Entity Comprehension.");
//        logger.log("Received event: " + event);
//        logger.log("containing " + event.getRecords().size() + " records.");
//
//        ChannelMessages channelMessages = gson.fromJson(event.getRecords().get(0).getSNS().getMessage(), ChannelMessages.class);
//
//        channelMessagesHandler.handle(channelMessages);
//        return "ok";
//    }

    @Override
    public String apply(SNSEvent event) {
        LOG.debug("Starting SnsMessage Entity Comprehension.");
        LOG.debug("Received event: " + event);
        LOG.debug("containing " + event.getRecords().size() + " records.");

        ChannelMessages channelMessages = gson.fromJson(event.getRecords().get(0).getSNS().getMessage(), ChannelMessages.class);

        channelMessagesHandler.handle(channelMessages);
        return "ok";
    }
}
