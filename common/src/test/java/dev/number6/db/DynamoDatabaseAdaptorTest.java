package dev.number6.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import dev.number6.db.adaptor.DatabaseServiceConfigurationAdaptor;
import dev.number6.db.adaptor.DynamoBasicDatabaseAdaptor;
import dev.number6.db.model.ChannelComprehensionSummary;
import dev.number6.db.port.DatabaseConfigurationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DynamoDatabaseAdaptorTest {

    private static final String OVERRIDE_TABLE_NAME = "OverrideTableName";
    private final DynamoDBMapper mapper = mock(DynamoDBMapper.class);
    private final DynamoDBMapperConfig dynamoDBMapperConfig = mock(DynamoDBMapperConfig.class);
    private final DatabaseConfigurationPort dbConfig = mock(DatabaseServiceConfigurationAdaptor.class);
    private DynamoBasicDatabaseAdaptor testee;

    @BeforeEach
    void setup() {
        DynamoDBMapperConfig.TableNameOverride tableNameOverride = new DynamoDBMapperConfig.TableNameOverride(OVERRIDE_TABLE_NAME);
        when(dynamoDBMapperConfig.getTableNameOverride()).thenReturn(tableNameOverride);
        when(dbConfig.getDynamoDBMapperConfig()).thenReturn(dynamoDBMapperConfig);
        testee = new DynamoBasicDatabaseAdaptor(mapper, dbConfig);
    }

    @Test
    void savesComprehensionSummary() {
        List<String> channelNames = RDG.list(RDG.string(20), 1).next();
        LocalDate comprehensionDate = LocalDate.now();

        ArgumentCaptor<DynamoDBMapperConfig> configCaptor = ArgumentCaptor.forClass(DynamoDBMapperConfig.class);
        ArgumentCaptor<ChannelComprehensionSummary> summaryCaptor = ArgumentCaptor.forClass(ChannelComprehensionSummary.class);
        testee.createNewSummaryForChannels(channelNames, comprehensionDate);

        verify(mapper).save(summaryCaptor.capture(), configCaptor.capture());
        assertThat(summaryCaptor.getValue().getChannelName()).isEqualTo(channelNames.get(0));
        assertThat(summaryCaptor.getValue().getComprehensionDate()).isEqualTo(comprehensionDate);
        assertThat(configCaptor.getValue().getTableNameOverride().getTableName()).isEqualTo(OVERRIDE_TABLE_NAME);
    }

    @Test
    void savesComprehensionSummaryForEachChannel() {
        Collection<String> channelNames = RDG.list(RDG.string(), Range.closed(3, 20)).next();
        LocalDate comprehensionDate = LocalDate.now();

        ArgumentCaptor<DynamoDBMapperConfig> configCaptor = ArgumentCaptor.forClass(DynamoDBMapperConfig.class);
        ArgumentCaptor<ChannelComprehensionSummary> summaryCaptor = ArgumentCaptor.forClass(ChannelComprehensionSummary.class);
        testee.createNewSummaryForChannels(channelNames, comprehensionDate);

        verify(mapper, times(channelNames.size())).save(summaryCaptor.capture(), configCaptor.capture());

        assertThat(summaryCaptor.getAllValues().stream().map(ChannelComprehensionSummary::getChannelName).collect(Collectors.toList())).containsExactlyInAnyOrderElementsOf(channelNames);
        summaryCaptor.getAllValues().forEach(c -> assertThat(c.getComprehensionDate()).isEqualTo(comprehensionDate));
        configCaptor.getAllValues().forEach(c -> assertThat(c.getTableNameOverride().getTableName()).isEqualTo(OVERRIDE_TABLE_NAME));
    }
}