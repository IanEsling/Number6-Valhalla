package dev.number6.db.adaptor;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import dev.number6.comprehend.results.ComprehensionResults;
import dev.number6.comprehend.results.PresentableEntityResults;
import dev.number6.comprehend.results.PresentableKeyPhrasesResults;
import dev.number6.comprehend.results.PresentableSentimentResults;
import dev.number6.db.model.ChannelComprehensionSummary;
import dev.number6.db.port.DatabaseConfigurationPort;
import dev.number6.db.port.FullDatabasePort;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.Collection;
import java.util.function.BiConsumer;

@Singleton
public class DynamoFullDatabaseAdaptor extends DynamoBasicDatabaseAdaptor implements FullDatabasePort {

    public DynamoFullDatabaseAdaptor(IDynamoDBMapper mapper, DatabaseConfigurationPort dbConfig) {
        super(mapper, dbConfig);
    }

    @Override
    public void createNewSummaryForChannels(Collection<String> channelNames, LocalDate comprehensionDate) {
        channelNames.forEach(c -> createNewSummaryForChannel(c, comprehensionDate));
    }

    @Override
    public void save(PresentableSentimentResults results) {

        save(results, (s, r) -> {
            s.setSentimentTotals(r.getSentimentTotals());
            s.setSentimentScoreTotals(r.getSentimentScoreTotals());
        });
    }

    @Override
    public void save(PresentableEntityResults results) {

        save(results, (s, r) -> s.setEntityTotals(r.getResults()));
    }

    @Override
    public void save(PresentableKeyPhrasesResults results) {

        save(results, (s, r) -> s.setKeyPhrasesTotals(r.getResults()));
    }

    private <T extends ComprehensionResults<?>> void save(T results,
                                                          BiConsumer<ChannelComprehensionSummary, T> summaryUpdater) {

        ChannelComprehensionSummary dbSummaries = mapper.load(ChannelComprehensionSummary.class,
                results.getChannelName(),
                results.getComprehensionDate(),
                dbConfig.getDynamoDBMapperConfig());

        summaryUpdater.accept(dbSummaries, results);

        try {
            mapper.save(dbSummaries, dbConfig.getDynamoDBMapperConfig());
        } catch (ConditionalCheckFailedException e) {
            save(results, summaryUpdater);
        }
    }

    private void createNewSummaryForChannel(String channelName, LocalDate comprehensionDate) {
        mapper.save(new ChannelComprehensionSummary(channelName, comprehensionDate), dbConfig.getDynamoDBMapperConfig());
    }
}

