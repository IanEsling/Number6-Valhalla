package dev.number6.db.mn;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class DynamoFactory {

    @Singleton
    public DynamoDBMapper dbMapper() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        return new DynamoDBMapper(client);
    }
}
