package dev.number6.sentiment;

import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;
import io.micronaut.function.FunctionBean;

@FunctionBean("msg-sentiment")
public class ChannelMessagesRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesRequestHandler(ChannelMessagesHandler channelMessagesHandler) {
        super(channelMessagesHandler);
    }
}
