package dev.number6.db.port;

import java.time.LocalDate;
import java.util.Collection;

public interface BasicDatabasePort {

    void createNewSummaryForChannels(Collection<String> channelNames, LocalDate comprehensionDate);
}
