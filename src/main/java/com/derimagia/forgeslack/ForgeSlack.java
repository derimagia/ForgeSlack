package com.derimagia.forgeslack;

import com.derimagia.forgeslack.handler.ForgeEventHandler;
import com.derimagia.forgeslack.slack.SlackRelay;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ForgeSlack.modId, useMetadata=true, acceptableRemoteVersions = "*", guiFactory = "com.derimagia.forgeslack.client.gui.ForgeSlackConfigGuiFactory")
public class ForgeSlack {
    public static final String modId = "forgeslack";

    public static Logger logger;
    private static SlackRelay slackRelay;

    private static Configuration config;
    private static boolean enabled = false;
    private static String slackToken = "";
    private static String channel = "";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();

        if (enabled) {
            MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        }
    }

//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent evt)
//    {
//        ForgeSlackConfig.sync();
//    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (enabled) {
            slackRelay = new SlackRelay(channel, slackToken);
            slackRelay.startup();
        }
    }
  
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        if (enabled) {
            slackRelay.shutdown();
            slackRelay = null;
        }
    }

    /**
     * Sync the config.
     */
    private static void syncConfig()
    {
        enabled = config.getBoolean("enabled", Configuration.CATEGORY_GENERAL, true, "Whether ForgeSlack is enabled.");
        slackToken = config.getString("slackToken", Configuration.CATEGORY_GENERAL, "", "Token Slack provides to Accept Slack Messages");
        channel = config.getString("channel", Configuration.CATEGORY_GENERAL, "#general", "Slack Channel to Listen/Send on");

        if (channel.isEmpty() || slackToken.isEmpty()) {
            enabled = false;
            ForgeSlack.logger.error("Either Slack Channel or Slack Token is empty. ForgeSlack will be disabled.");
        }

        if (config.hasChanged()) {
            ForgeSlack.logger.error("Loading Configuration.");
            config.save();
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    public static SlackRelay getSlackRelay() {
        return slackRelay;
    }
}
