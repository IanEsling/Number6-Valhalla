package dev.number6.slackposter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import dev.number6.slack.LambdaLoggingFacade;
import io.micronaut.function.FunctionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

@FunctionBean("slack-poster")
public class SlackPosterRequestHandler implements Function<DynamodbEvent, String> {

    private static final Logger LOG = LoggerFactory.getLogger(SlackPosterRequestHandler.class);
    private final SlackService slackService;
    LambdaLogger logger = new LambdaLoggingFacade(LOG);

    public SlackPosterRequestHandler(SlackService slackService) {
        this.slackService = slackService;
    }

//    @Override
//    public String handleRequest(DynamodbEvent o, Context context) {
//        context.getLogger().log("processing " + o.getRecords().size() + " records on event");
//        context.getLogger().log("processing " + o.getRecords());
//
//        try {
//            o.getRecords().forEach(r -> {
//                if (!r.getEventName().equalsIgnoreCase("REMOVE")) {
//                    Map<String, AttributeValue> vals = r.getDynamodb().getNewImage();
//                    slackService.handleNewImage(new ChannelSummaryImage(vals), context.getLogger());
//
//                }
//            });
//        } catch (Exception e) {
//            context.getLogger().log("exception caught posting to Slack: " + e.getMessage());
//        }
//        return "ok";
//    }

    @Override
    public String apply(DynamodbEvent event) {
        LOG.debug("processing " + event.getRecords().size() + " records on event");
        LOG.debug("processing " + event.getRecords());

        try {
            event.getRecords().forEach(r -> {
                if (!r.getEventName().equalsIgnoreCase("REMOVE")) {
                    Map<String, AttributeValue> vals = r.getDynamodb().getNewImage();
                    slackService.handleNewImage(new ChannelSummaryImage(vals), logger);
                }
            });
        } catch (Exception e) {
            LOG.debug("exception caught posting to Slack: " + e.getMessage());
        }
        return "ok";
    }
}
