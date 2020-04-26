package dev.number6.sentiment

import dev.number6.comprehend.results.PresentableSentimentResults
import dev.number6.message.ChannelMessages
import dev.number6.sentiment.dagger.DaggerTestChannelMessageSentimentComprehensionComponent
import dev.number6.sentiment.dagger.FakeComprehensionResultsModule
import dev.number6.sentiment.dagger.TestChannelMessageSentimentComprehensionComponent
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ChannelMessagesSentimentComprehensionComponentIntegrationTest {
    var testee: TestChannelMessageSentimentComprehensionComponent = DaggerTestChannelMessageSentimentComprehensionComponent.create()

    @Test
    fun providesChannelHandler() {
        val results = mockk<PresentableSentimentResults>()
        val handler = testee.getChannelMessagesHandler()
        val f = testee.getResultsFunction() as FakeComprehensionResultsModule.ConfigurableResultsFunction
        f.setPresentableSentimentResults(results)
        handler.handle(mockk<ChannelMessages>())
        val consumer = testee.getConsumer() as FakeComprehensionResultsModule.RecordingComprehensionResultsConsumer
        Assertions.assertThat(consumer.getResultsConsumed()).hasSize(1)
        Assertions.assertThat(consumer.getResultsConsumed()[0]).isEqualTo(results)
    }
}