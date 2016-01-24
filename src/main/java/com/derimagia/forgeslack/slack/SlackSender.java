package com.derimagia.forgeslack.slack;

import com.derimagia.forgeslack.handler.ConfigurationHandler;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

/**
 * @author derimagia
 */
public  class SlackSender {
    private static SlackSender instance;
    public SlackApi api;

    public SlackSender() {
        api = new SlackApi(ConfigurationHandler.slackIncomingWebHook);
    }

    /**
     * Returns the Instance
     * @return SlackSender
     */
    public static SlackSender getInstance() {
        if (instance == null) {
            instance = new SlackSender();
        }

        return instance;
    }

    /**
     * Sends a Slack Message in a new Thread
     * @param message
     * @param username
     */
    public void send(String message, String username) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setText(message);
        slackMessage.setUsername(username);
        slackMessage.setIcon("https://mcapi.ca/avatar/3d/" + username);

        // Send in a new thread so it doesn't block the game.
        Thread thread = new Thread(new SlackSendThread(slackMessage));
        thread.start();
    }
}