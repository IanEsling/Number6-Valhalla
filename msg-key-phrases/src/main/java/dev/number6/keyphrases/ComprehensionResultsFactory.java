package dev.number6.keyphrases;

import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.db.port.DatabasePort;
import dev.number6.message.ChannelMessagesComprehensionHandler;
import dev.number6.message.ChannelMessagesToComprehensionResultsFunction;
import dev.number6.message.ComprehensionResultsConsumer;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class ComprehensionResultsFactory {

    @Singleton
    public ChannelMessagesToComprehensionResultsFunction<PresentableKeyPhrasesResults> providesMessageToEntityResults(ComprehensionPort comprehensionPort) {
        return comprehensionPort::getKeyPhrasesForSlackMessages;
    }

    @Singleton
    public ComprehensionResultsConsumer<PresentableKeyPhrasesResults> providesEntityResultsConsumer(DatabasePort databasePort) {
        return databasePort::save;
    }

    @Singleton
    public ChannelMessagesComprehensionHandler<PresentableKeyPhrasesResults> handler(
            ChannelMessagesToComprehensionResultsFunction<PresentableKeyPhrasesResults> function,
            ComprehensionResultsConsumer<PresentableKeyPhrasesResults> consumer) {
        return new ChannelMessagesComprehensionHandler<>(function, consumer);
    }
}
