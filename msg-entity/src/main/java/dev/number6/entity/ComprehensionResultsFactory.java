package dev.number6.entity;

import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.db.port.DatabasePort;
import dev.number6.message.ChannelMessagesComprehensionHandler;
import dev.number6.message.ChannelMessagesToComprehensionResultsFunction;
import dev.number6.message.ComprehensionResultsConsumer;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class ComprehensionResultsFactory {

    @Singleton
    public ChannelMessagesToComprehensionResultsFunction<PresentableEntityResults> providesMessageToEntityResults(ComprehensionPort comprehensionPort) {
        return comprehensionPort::getEntitiesForSlackMessages;
    }

    @Singleton
    public ComprehensionResultsConsumer<PresentableEntityResults> providesEntityResultsConsumer(DatabasePort databasePort) {
        return databasePort::save;
    }

    @Singleton
    public ChannelMessagesComprehensionHandler<PresentableEntityResults> handler(
            ChannelMessagesToComprehensionResultsFunction<PresentableEntityResults> function,
            ComprehensionResultsConsumer<PresentableEntityResults> consumer) {
        return new ChannelMessagesComprehensionHandler<>(function, consumer);
    }
}
