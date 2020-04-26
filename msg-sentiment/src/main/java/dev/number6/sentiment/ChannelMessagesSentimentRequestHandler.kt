package dev.number6.sentiment

import dev.number6.message.ChannelMessagesNotificationRequestHandler
import dev.number6.sentiment.dagger.DaggerChannelMessagesSentimentComprehensionComponent

class ChannelMessagesSentimentRequestHandler : ChannelMessagesNotificationRequestHandler(DaggerChannelMessagesSentimentComprehensionComponent.create().getChannelMessagesHandler())