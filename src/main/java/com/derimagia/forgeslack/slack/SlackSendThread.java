package com.derimagia.forgeslack.slack;

import net.gpedro.integrations.slack.SlackMessage;

/**
 * @author derimagia
 */
public class SlackSendThread implements Runnable {
    private SlackMessage slackMessage;

    public SlackSendThread(SlackMessage message) {
        slackMessage = message;
    }

    @Override
    public void run() {
        SlackSender.getInstance().api.call(slackMessage);
    }
}
