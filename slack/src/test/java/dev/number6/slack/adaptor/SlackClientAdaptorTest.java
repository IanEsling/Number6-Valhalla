package dev.number6.slack.adaptor;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import dev.number6.slack.CallResponse;
import dev.number6.slack.port.HttpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlackClientAdaptorTest {

    private final Gson gson = new Gson();
    private final TestObjectGenerator testObjectGenerator = new TestObjectGenerator();
    @Mock
    LambdaLogger logger;
    @Mock
    HttpPort http;
    @InjectMocks
    SlackClientAdaptor testee;
    private TestResponseObject testResponseObject;

    @BeforeEach
    void setup() {
        testResponseObject = testObjectGenerator.next();
    }

    @Test
    void getRequestWithResponseObject() {
        when(http.get(any(), any())).thenReturn(new CallResponse(gson.toJson(testResponseObject)));
        Optional<TestResponseObject> response = testee.getSlackResponse(SlackClientAdaptor.CHANNEL_LIST_URL, TestResponseObject.class, logger);

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get()).isEqualTo(testResponseObject);
    }

    @Test
    void postWithBodyNoResponse() {
        when(http.post(any(), any(), any())).thenReturn(new CallResponse(gson.toJson(testResponseObject)));
        Optional<String> response = testee.getSlackResponse(SlackClientAdaptor.CHANNEL_HISTORY_URL, logger, "BODY");

        Assertions.assertThat(response.isPresent()).isFalse();
    }

    @Test
    void postWithBodyAndResponse() {
        when(http.post(any(), any(), any())).thenReturn(new CallResponse(gson.toJson(testResponseObject)));
        Optional<TestResponseObject> response = testee.getSlackResponse(SlackClientAdaptor.CHANNEL_HISTORY_URL, "BODY", TestResponseObject.class, logger);

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get()).isEqualTo(testResponseObject);
    }

    private static class TestResponseObject {
        private final int field1;
        private final String field2;
        private final Double field3;

        TestResponseObject(int field1, String field2, Double field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestResponseObject that = (TestResponseObject) o;
            return field1 == that.field1 &&
                    Objects.equals(field2, that.field2) &&
                    Objects.equals(field3, that.field3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field1, field2, field3);
        }
    }

    static class TestObjectGenerator implements Generator<TestResponseObject> {

        @Override
        public TestResponseObject next() {
            return new TestResponseObject(RDG.integer(999).next(),
                    RDG.string(30).next(),
                    RDG.doubleVal(9999d).next());
        }
    }
}