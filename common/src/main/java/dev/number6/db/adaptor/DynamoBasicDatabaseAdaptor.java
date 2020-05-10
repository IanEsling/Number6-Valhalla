package dev.number6.db.adaptor;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import dev.number6.db.model.ChannelComprehensionSummary;
import dev.number6.db.port.DatabaseConfigurationPort;
import dev.number6.db.port.BasicDatabasePort;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.Collection;

@Singleton
public class DynamoBasicDatabaseAdaptor implements BasicDatabasePort {

    protected final IDynamoDBMapper mapper;
    protected final DatabaseConfigurationPort dbConfig;

    public DynamoBasicDatabaseAdaptor(IDynamoDBMapper mapper, DatabaseConfigurationPort dbConfig) {
        this.mapper = mapper;
        this.dbConfig = dbConfig;
    }

    @Override
    public void createNewSummaryForChannels(Collection<String> channelNames, LocalDate comprehensionDate) {
        channelNames.forEach(c -> createNewSummaryForChannel(c, comprehensionDate));
    }

    private void createNewSummaryForChannel(String channelName, LocalDate comprehensionDate) {
        mapper.save(new ChannelComprehensionSummary(channelName, comprehensionDate), dbConfig.getDynamoDBMapperConfig());
    }
}

