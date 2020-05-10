package dev.number6.entity;

import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;
import io.micronaut.function.FunctionBean;

@FunctionBean("msg-entities")
public class ChannelMessagesRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesRequestHandler(ChannelMessagesHandler channelMessagesHandler) {
        super(channelMessagesHandler);
    }
}
