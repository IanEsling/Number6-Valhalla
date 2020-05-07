package dev.number6.slackreader.core;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.sns.AmazonSNS;
import com.google.gson.Gson;
import dev.number6.slackreader.SlackReader;
import dev.number6.slackreader.SlackReaderRequestHandler;
import dev.number6.slackreader.adaptor.SlackReaderAdaptor;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MicronautTest
class SlackReaderIntegrationTest {

//    @Inject
//    SlackReaderAdaptor reader;
//    @Inject
//    AmazonDynamoDB amazonDynamoDB;
//    @Inject
//    AmazonSNS amazonSNS;
//    @Inject
//    private Clock clock;
//    @Inject
//    private SlackReaderRequestHandler testee;
//    private final TestSlackReaderComponent testee = DaggerTestSlackReaderComponent.create();

//    @MockBean(SlackPort.class)
//    SlackPort recordingSlackPort() {
//        return new RecordingSlackReaderAdaptor(mock(HttpPort.class));
//    }

//    @MockBean(AmazonDynamoDB.class)
//    FakeAmazonDynamoDB fakeAmazonDynamoDB() {
//        return new FakeAmazonDynamoDB();
//    }

//    @MockBean(AmazonSNS.class)
//    FakeAmazonSns fakeAmazonSNS() {
//        return new FakeAmazonSns();
//    }

//    @Test
//    void handleEventWithoutDate() {
//
//        Clock clock = testee.getClock();
//        Map<String, Object> triggerEvent = new HashMap<>();
//        testEventComprehensionDate(triggerEvent, LocalDate.now(clock));
//    }
//
//    @Test
//    void handleEventWithDate() {
//        Clock clock = testee.getClock();
//        Map<String, Object> triggerEvent = new HashMap<>();
//        triggerEvent.put(SlackReader.COMPREHENSION_DATE_FIELD_NAME, LocalDate.now(clock).toString());
//        testEventComprehensionDate(triggerEvent, LocalDate.now(clock));
//    }

    private void testEventComprehensionDate(Map<String, Object> triggerEvent, LocalDate comprehensionDate) {

//        RecordingSlackReaderAdaptor reader = (RecordingSlackReaderAdaptor) testee.getSlackPort();
//        FakeAmazonDynamoDB fakeDynamo = testee.getFakeAmazonDynamoClient();
//        FakeAmazonSns fakeSns = testee.getFakeAmazonSns();

//        if (triggerEvent == null) {
//            triggerEvent = new HashMap<>();
//        } else {
//            triggerEvent.put(SlackReader.COMPREHENSION_DATE_FIELD_NAME, comprehensionDate.toString());
//        }
//        testee.apply(triggerEvent);

//        List<ChannelComprehensionSummary> savedChannelSummaries = ((FakeAmazonDynamoDB)amazonDynamoDB).getSavedObjects().stream()
//                .map(o -> ((ChannelComprehensionSummary) o))
//                .collect(Collectors.toList());
//
//        savedChannelSummaries.forEach(s -> assertThat(s.getComprehensionDate()).isEqualTo(comprehensionDate));
//        assertThat(savedChannelSummaries.stream().map(ChannelComprehensionSummary::getChannelName).collect(Collectors.toList()))
//                .containsExactlyInAnyOrderElementsOf(reader.getChannelNames());
//
//        Collection<ChannelMessages> channelMessages = ((FakeAmazonSns)amazonSNS).getPublishedMessages().stream()
//                .map(p -> gson.fromJson(p, ChannelMessages.class))
//                .collect(Collectors.toList());
//
//        channelMessages.forEach(cm -> assertThat(cm.getChannelName()).isIn(reader.getChannelNames()));
//        channelMessages.forEach(cm -> assertThat(cm.getMessages()).containsExactlyInAnyOrderElementsOf(reader.getMessagesForChannelName(cm.getChannelName())));
    }
}
