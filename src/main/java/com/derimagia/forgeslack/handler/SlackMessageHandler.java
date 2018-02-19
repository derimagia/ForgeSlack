package com.derimagia.forgeslack.handler;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.User;
import com.derimagia.forgeslack.ForgeSlack;
import com.derimagia.forgeslack.slack.SlackRelay;
import com.fasterxml.jackson.databind.JsonNode;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlackMessageHandler implements EventListener {
    // Mainly Copied from ForgeHooks
    private static final Pattern URL_PATTERN = Pattern.compile(
            //         schema                          ipv4            OR        namespace                 port     path    ends
            //   |-----------------|        |-------------------------|  |-------------------------|    |---------| |--|   |----|
            "<((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?)(?:[|]((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?))?>",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void onMessage(JsonNode jsonMessage) {
        SlackRelay slackRelay = SlackRelay.getInstance();

        // @TODO: Bot ID is different from user id, so this isn't working right now.
        if (jsonMessage.hasNonNull("bot_id")) {
            if (jsonMessage.findPath("bot_id").textValue().equals(slackRelay.getBotId())) {
                return;
            }
        }

        String sub_type;
        if (jsonMessage.hasNonNull("subtype")) {
            sub_type = jsonMessage.get("subtype").asText();

            if (sub_type.equals("bot_message")) {
                return;
            }
        }

        if (!jsonMessage.hasNonNull("channel") || !jsonMessage.hasNonNull("text")) {
            ForgeSlack.log.error(String.format("Invalid RTM Message for onMessage: %s", jsonMessage));
            return;
        }

        // Check the channel is our channel
        String channel = jsonMessage.get("channel").asText();
        if (!channel.equals(slackRelay.getChannelId())) {
            return;
        }

        String text = jsonMessage.get("text").asText();

        String username;
        if (jsonMessage.hasNonNull("username")) {
            username = jsonMessage.findPath("username").asText();
        } else if (jsonMessage.hasNonNull("user")) {
            String userid = jsonMessage.findPath("user").asText();
            User user = slackRelay.getUsernameFromId(userid);
            username = user != null ? user.getName() : userid;
        } else {
            ForgeSlack.log.error(String.format("Invalid RTM Message for onMessage: %s", jsonMessage));
            return;
        }

        String mcMessage = String.format("[Slack] <%s> %s", username, cleanMessageLinks(text));
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            server.getPlayerList().sendMessage(ForgeHooks.newChatWithLinks(mcMessage));
        }
    }

    /**
     * Slack returns the urls wrapped with "< http://example.com | example.com >" (Without spaces)
     *
     * This removes that so that it's just "http://example.com".
     *
     * This also fixes ForgeHooks.newChatWithLinks so that it can then replace the links correctly.
     *
     * @param text - The text to clean.
     */
    private String cleanMessageLinks(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        int lastEnd = 0;
        StringBuilder cleanedString = new StringBuilder();

        // Find all urls
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // Append the previous left overs.
            cleanedString.append(text.substring(lastEnd, start));

            // Append URL
            cleanedString.append(matcher.group(1));
            lastEnd = end;
        }

        cleanedString.append(text.substring(lastEnd));

        return cleanedString.toString();
    }
}