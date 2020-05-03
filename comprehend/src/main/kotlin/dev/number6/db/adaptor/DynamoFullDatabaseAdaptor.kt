package dev.number6.db.adaptor

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import dev.number6.comprehend.results.ComprehensionResults
import dev.number6.comprehend.results.PresentableEntityResults
import dev.number6.comprehend.results.PresentableKeyPhrasesResults
import dev.number6.comprehend.results.PresentableSentimentResults
import dev.number6.db.model.ChannelComprehensionSummary
import dev.number6.db.port.DatabaseConfigurationPort
import dev.number6.db.port.FullDatabasePort
import java.util.function.BiConsumer

class DynamoFullDatabaseAdaptor(private val mapper: DynamoDBMapper, private val dbConfig: DatabaseConfigurationPort) :
        DynamoBasicDatabaseAdaptor(mapper, dbConfig), FullDatabasePort {

    override fun save(results: PresentableSentimentResults) {
        save(results, BiConsumer { s: ChannelComprehensionSummary, r: PresentableSentimentResults ->
            s.sentimentTotals = r.sentimentTotals
            s.sentimentScoreTotals = r.sentimentScoreTotals
        })
    }

    override fun save(results: PresentableEntityResults) {
        save(results, BiConsumer { s: ChannelComprehensionSummary, r: PresentableEntityResults -> s.entityTotals = r.results })
    }

    override fun save(results: PresentableKeyPhrasesResults) {
        save(results, BiConsumer { s: ChannelComprehensionSummary, r: PresentableKeyPhrasesResults -> s.keyPhrasesTotals = r.results })
    }

    private fun <T : ComprehensionResults<*>> save(results: T,
                                                   summaryUpdater: BiConsumer<ChannelComprehensionSummary, T>) {
        val dbSummaries = mapper.load(ChannelComprehensionSummary::class.java,
                results.channelName,
                results.comprehensionDate,
                dbConfig.dynamoDBMapperConfig)
        summaryUpdater.accept(dbSummaries, results)
        try {
            mapper.save(dbSummaries, dbConfig.dynamoDBMapperConfig)
        } catch (e: ConditionalCheckFailedException) {
            save(results, summaryUpdater)
        }
    }
}