package dev.number6.entity;

import dev.number6.message.ChannelMessages;
import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;

public class ChannelMessagesEntityRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesEntityRequestHandler() {
        super(new ChannelMessagesHandler() {
            @Override
            public void handle(ChannelMessages channelMessages) {

            }
        });
    }
}
