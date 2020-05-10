package dev.number6.keyphrases;

import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;
import io.micronaut.function.FunctionBean;

@FunctionBean("msg-key-phrases")
public class ChannelMessagesRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesRequestHandler(ChannelMessagesHandler channelMessagesHandler) {
        super(channelMessagesHandler);
    }
}
