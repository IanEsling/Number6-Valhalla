package dev.number6.sentiment;

import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableSentimentResults;
import dev.number6.db.port.FullDatabasePort;
import dev.number6.message.ChannelMessagesComprehensionHandler;
import dev.number6.message.ChannelMessagesToComprehensionResultsFunction;
import dev.number6.message.ComprehensionResultsConsumer;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class ComprehensionResultsFactory {

    @Singleton
    public ChannelMessagesToComprehensionResultsFunction<PresentableSentimentResults> providesMessageToEntityResults(ComprehensionPort comprehensionPort) {
        return comprehensionPort::getSentimentForSlackMessages;
    }

    @Singleton
    public ComprehensionResultsConsumer<PresentableSentimentResults> providesEntityResultsConsumer(FullDatabasePort databasePort) {
        return databasePort::save;
    }

    @Singleton
    public ChannelMessagesComprehensionHandler<PresentableSentimentResults> handler(
            ChannelMessagesToComprehensionResultsFunction<PresentableSentimentResults> function,
            ComprehensionResultsConsumer<PresentableSentimentResults> consumer) {
        return new ChannelMessagesComprehensionHandler<>(function, consumer);
    }
}
