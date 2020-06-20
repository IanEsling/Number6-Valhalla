package dev.number6.comprehend.adaptor;

import com.amazonaws.services.comprehend.AmazonComprehend;
import dev.number6.comprehend.port.ComprehensionPort;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.comprehend.results.PresentableSentimentResults;
import dev.number6.message.ChannelMessages;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

import javax.inject.Singleton;


@Singleton
@Requires(env = Environment.TEST)
public class MockComprehensionAdaptor implements ComprehensionPort {

    @Override
    public PresentableEntityResults getEntitiesForSlackMessages(ChannelMessages channelMessages) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PresentableSentimentResults getSentimentForSlackMessages(ChannelMessages channelMessages) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PresentableKeyPhrasesResults getKeyPhrasesForSlackMessages(ChannelMessages channelMessages) {
        throw new UnsupportedOperationException();
    }
}
