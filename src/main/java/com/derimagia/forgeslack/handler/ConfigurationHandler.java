package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.ForgeSlack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @author derimagia
 */
public class ConfigurationHandler
{
    public static Configuration configuration;
    public static boolean enabled = true;
    public static String slackIncomingWebHook = "";
    public static String slackToken = "";
    public static int jettyServerPort = 8085;

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
        slackIncomingWebHook = configuration.getString("slackIncomingWebHook", Configuration.CATEGORY_GENERAL, "", "Slack Incoming WebHook URL");
        slackToken = configuration.getString("slackToken", Configuration.CATEGORY_GENERAL, "", "Token Slack provides to Accept Slack Messages");
        jettyServerPort = configuration.getInt("port", Configuration.CATEGORY_GENERAL, 8085, 1, 65535, "Port for Web Server to process Slack Messages");

        if (slackIncomingWebHook.isEmpty()) {
            enabled = false;
            ForgeSlack.log.error("Slack Incoming WebHook is empty. Disabling Mod.");
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}