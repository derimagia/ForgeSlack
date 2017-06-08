package com.derimagia.forgeslack.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Authentication;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;
import allbegray.slack.webapi.method.chats.ChatPostMessageMethod;
import com.derimagia.forgeslack.ForgeSlack;
import com.derimagia.forgeslack.handler.ConfigurationHandler;
import com.derimagia.forgeslack.handler.SlackMessageHandler;

import java.util.HashMap;
import java.util.List;

public class SlackRelay {
    private static SlackRelay instance;
    private SlackWebApiClient api;
    private SlackRealTimeMessagingClient rtmapi;
    private String channel;
    private String channelId;
    private Authentication auth;
    private HashMap<String, User> users;

    private SlackRelay() {
        channel = ConfigurationHandler.channel;
        users = new HashMap<>();
    }

    /**
     * Returns the Instance
     */
    public static SlackRelay getInstance() {
        if (instance == null) {
            instance = new SlackRelay();
        }

        return instance;
    }

    /**
     * Fetches and caches a usermap
     */
    private void fetchUsers() {
        List<User> userList = api.getUserList();
        for (User u : userList) {
            users.put(u.getId(), u);
        }
    }

    /**
     * Fetches and caches the channel id
     */
    private void fetchChannel() {
        List<Channel> channels = api.getChannelList();
        for (Channel c: channels) {
            String fullName = "#" + c.getName();
            if (fullName.equals(channel)) {
                channelId = c.getId();
                return;
            }
        }
    }

    /**
     * Connects the relay
     */
    public void startup() {
        api = SlackClientFactory.createWebApiClient(ConfigurationHandler.slackToken);
        rtmapi = SlackClientFactory.createSlackRealTimeMessagingClient(ConfigurationHandler.slackToken);
        rtmapi.addListener(Event.MESSAGE, new SlackMessageHandler());

        (new Thread(() -> {
            fetchChannel();
            fetchUsers();
            auth = api.auth();
            rtmapi.connect();
            sendMessage("Server is online");
        })).start();
    }

    /**
     * Shutdown the Relay
     */
    public void shutdown() {
        sendMessage("Server is now offline");

        if (rtmapi != null) {
            rtmapi.close();
        }

        if (api != null) {
            api.shutdown();
        }
    }

    /**
     * Sends a Slack Message
     *
     * @param message - Message to be sent
     */
    public void sendMessage(String message) {
        sendMessage(message, "");
    }

    /**
     * Sends a Slack Message in a new Thread
     * 
     * @param message - Message to be sent
     * @param username - Minecraft Username of User.
     */
    public void sendMessage(String message, String username) {
        ChatPostMessageMethod chatMessage = new ChatPostMessageMethod(channel, message);

        if (!username.isEmpty()) {
            chatMessage.setUsername(username);
            // @TODO: Figure out if we can support 3d avatars again.
            // chatMessage.setIcon_url("https://mcapi.ca/avatar/3d/" + username);
            chatMessage.setIcon_url("https://mcapi.ca/avatar/" + username);
        }

        // @TODO: Use the rtm instead.
        (new Thread(() -> {
            try {
                if (api != null) {
                    api.postMessage(chatMessage);
                } else {
                    ForgeSlack.log.error(String.format("Tried to send slack message: '%s'. Slack Web API Client is not started.", chatMessage));
                }
            } catch (Exception e) {
                ForgeSlack.log.error(e.getMessage());
            }
        }, "ForgeSlack-message")).start();
    }

    /**
     * Gets the Bot ID of the auth.
     */
    public String getBotId() {
        return auth.getUser_id();
    }

    /**
     * Gets the channel id we are relaying.
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Gets a User from a userid.
     *
     * @param userId - Slack userid of the user.
     */
    public User getUsernameFromId(String userId) {
        User user = users.get(userId);

        if (user != null) {
            return user;
        }

        // Try one more time if we didn't find it by reloading the cache.
        fetchUsers();
        return users.get(userId);
    }
}