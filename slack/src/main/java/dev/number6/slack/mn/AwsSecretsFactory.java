package dev.number6.slack.mn;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class AwsSecretsFactory {

    @Singleton
    public AWSSecretsManager awsSecretsManager() {
        return AWSSecretsManagerClientBuilder.defaultClient();
    }
}
