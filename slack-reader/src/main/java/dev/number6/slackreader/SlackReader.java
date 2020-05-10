package dev.number6.slackreader;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dev.number6.db.port.BasicDatabasePort;
import dev.number6.slackreader.model.WorkspaceMessages;

import javax.inject.Singleton;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

@Singleton
public class SlackReader {

    public static final String COMPREHENSION_DATE_FIELD_NAME = "comprehensionDate";
    private final SlackService slackService;
    private final SnsService snsService;
    private final BasicDatabasePort dbService;
    private final Clock clock;

    public SlackReader(SlackService slackService,
                       SnsService snsService,
                       BasicDatabasePort dbService,
                       Clock clock) {

        this.slackService = slackService;
        this.snsService = snsService;
        this.dbService = dbService;
        this.clock = clock;
    }

    public void handle(Map<String, Object> event, LambdaLogger logger) {

        Object dateEvent = event.get(COMPREHENSION_DATE_FIELD_NAME);
        LocalDate summaryDate = dateEvent == null || "".equals(dateEvent) ? LocalDate.now(clock).minusDays(1) : LocalDate.parse(dateEvent.toString());

        WorkspaceMessages messages = slackService.getMessagesOnDate(summaryDate, logger);
        dbService.createNewSummaryForChannels(messages.getActiveChannelNames(), messages.getComprehensionDate());
        snsService.broadcastWorkspaceMessagesForActiveChannels(messages);
    }
}
