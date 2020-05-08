package dev.number6;

import dev.number6.slack.adaptor.OkHttpRequestAdaptor;
import org.assertj.core.api.Assertions;

public class SlackAssertions extends Assertions {

    public static RequestPortAssert assertThat(OkHttpRequestAdaptor callRequest) {
        return new RequestPortAssert(callRequest);
    }
}
