package dev.number6.generate;

import uk.org.fyodor.generators.RDG;

public class CommonRDG extends RDG {

    private final static ChannelMessagesGenerator channelMessagesGenerator = new ChannelMessagesGenerator();
    private final static ChannelComprehensionSummaryGenerator channelComprehensionSummaryGenerator = new ChannelComprehensionSummaryGenerator();

    public static ChannelComprehensionSummaryGenerator channelComprehensionSummary() {
        return channelComprehensionSummaryGenerator;
    }

    public static DetectSentimentResultGenerator detectSentimentResult() {
        return new DetectSentimentResultGenerator();
    }

    public static ChannelMessagesGenerator channelMessages() {
        return channelMessagesGenerator;
    }

    public static float sentimentScoreFloat() {
        return doubleVal(1d).next().floatValue();
    }
}
