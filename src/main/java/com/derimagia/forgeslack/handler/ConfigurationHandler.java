package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.ForgeSlack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler
{
    private static Configuration configuration;
    public static boolean enabled = true;
    public static String slackToken = "";
    public static String channel = "";

    public static void init(File configFile)
    {
        if (configuration == null)
        {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        enabled = configuration.getBoolean("enabled", Configuration.CATEGORY_GENERAL, true, "Whether ForgeSlack is enabled.");
        slackToken = configuration.getString("slackToken", Configuration.CATEGORY_GENERAL, "", "Token Slack provides to Accept Slack Messages");
        channel = configuration.getString("channel", Configuration.CATEGORY_GENERAL, "#general", "Slack Channel to Listen/Send on");

        if (channel.isEmpty() || slackToken.isEmpty()) {
            enabled = false;
            ForgeSlack.log.error("Either Slack Channel or Slack Token is empty. ForgeSlack will be disabled.");
        }

        if (configuration.hasChanged()) {
            ForgeSlack.log.error("Loading Configuration.");
            configuration.save();
        }
    }
}