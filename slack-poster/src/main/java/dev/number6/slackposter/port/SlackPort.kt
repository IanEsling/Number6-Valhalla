package dev.number6.slackposter.port

import com.amazonaws.services.lambda.runtime.LambdaLogger
import dev.number6.slackposter.model.PresentableChannelSummary

interface SlackPort {
    fun postMessageToChannel(summary: PresentableChannelSummary, logger: LambdaLogger)
}