package dev.number6.sentiment;

import dev.number6.message.ChannelMessages;
import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;

public class ChannelMessagesSentimentRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesSentimentRequestHandler() {
        super(new ChannelMessagesHandler() {
            @Override
            public void handle(ChannelMessages channelMessages) {

            }
        });
    }
}
