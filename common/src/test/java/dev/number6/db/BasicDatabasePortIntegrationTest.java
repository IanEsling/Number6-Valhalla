package dev.number6.db;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import dev.number6.db.port.BasicDatabasePort;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MicronautTest
public class BasicDatabasePortIntegrationTest {

    IDynamoDBMapper mockDbMapper = mock(IDynamoDBMapper.class);
    @Inject
    BasicDatabasePort testee;

    @MockBean(IDynamoDBMapper.class)
    IDynamoDBMapper dbMapper() {
        return mockDbMapper;
    }

    @Test
    void wiring() {
        assertThat(testee).isNotNull();
    }
}