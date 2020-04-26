package dev.number6.keyphrases

import dev.number6.comprehend.results.PresentableKeyPhrasesResults
import dev.number6.keyphrases.dagger.DaggerTestChannelMessagesKeyPhrasesComprehensionComponent
import dev.number6.keyphrases.dagger.FakeComprehensionResultsModule
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ChannelMessagesKeyPhrasesComprehensionComponentIntegrationTest {
    var testee = DaggerTestChannelMessagesKeyPhrasesComprehensionComponent.create()

    @Test
    fun providesChannelHandler() {
        val results = mockk<PresentableKeyPhrasesResults>()
        val handler = testee.getChannelMessagesHandler()
        val f = testee.getResultsFunction() as FakeComprehensionResultsModule.ConfigurableResultsFunction
        f.setPresentableKeyPhrasesResults(results)
        handler.handle(mockk())
        val consumer = testee.getConsumer() as FakeComprehensionResultsModule.RecordingComprehensionResultsConsumer
        Assertions.assertThat(consumer.getResultsConsumed()).hasSize(1)
        Assertions.assertThat(consumer.getResultsConsumed()[0]).isEqualTo(results)
    }
}