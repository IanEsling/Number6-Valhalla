package dev.number6.slack.adaptor;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import dev.number6.generate.CommonRDG;
import dev.number6.slack.port.SecretsConfigurationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AWSSecretsAdaptorTest {

    private static final String SECRET_NAME = "Secret Name";
    private static final String SECRET_TOKEN = "Secret Token";
    private final SecretsConfigurationPort config = mock(SecretsConfigurationPort.class);
    private final AWSSecretsManager aws = mock(AWSSecretsManager.class);
    private final LambdaLogger logger = mock(LambdaLogger.class);
    private AWSSecretsAdaptor testee;
    private GetSecretValueResult result;

    @BeforeEach
    void setup() {

        result = new GetSecretValueResult().withSecretString(SECRET_TOKEN);
        when(config.getSlackTokenSecretName()).thenReturn(SECRET_NAME);
        when(aws.getSecretValue(any())).thenReturn(result);

        testee = new AWSSecretsAdaptor(aws, config);
    }

    @Test
    void getSecretFromAWS() {

        String secret = testee.getSlackTokenSecret(logger);
        assertThat(secret).isEqualTo(SECRET_TOKEN);
    }

    @Test
    void cacheSecretForFutureCalls() {

        assertThat(testee.getSlackTokenSecret(logger)).isEqualTo(SECRET_TOKEN);

        result = new GetSecretValueResult().withSecretString(CommonRDG.string().next());
        when(aws.getSecretValue(any())).thenReturn(result);

        assertThat(testee.getSlackTokenSecret(logger)).isEqualTo(SECRET_TOKEN);
        assertThat(testee.getSlackTokenSecret(logger)).isEqualTo(SECRET_TOKEN);
    }
}