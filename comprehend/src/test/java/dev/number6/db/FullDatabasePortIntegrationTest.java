package dev.number6.db;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import dev.number6.db.port.FullDatabasePort;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MicronautTest
public class FullDatabasePortIntegrationTest {

    IDynamoDBMapper mockDbMapper = mock(IDynamoDBMapper.class);

    @MockBean(IDynamoDBMapper.class)
    IDynamoDBMapper dbMapper() {
        return mockDbMapper;
    }
    @Inject
    FullDatabasePort testee;

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}