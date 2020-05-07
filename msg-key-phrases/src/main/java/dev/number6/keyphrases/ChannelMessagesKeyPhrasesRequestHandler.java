package dev.number6.keyphrases;

import dev.number6.message.ChannelMessages;
import dev.number6.message.ChannelMessagesHandler;
import dev.number6.message.ChannelMessagesNotificationRequestHandler;

public class ChannelMessagesKeyPhrasesRequestHandler extends ChannelMessagesNotificationRequestHandler {

    public ChannelMessagesKeyPhrasesRequestHandler() {
        super(new ChannelMessagesHandler() {
            @Override
            public void handle(ChannelMessages channelMessages) {

            }
        });
    }
}
