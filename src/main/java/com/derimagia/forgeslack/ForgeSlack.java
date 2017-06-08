package com.derimagia.forgeslack;

import com.derimagia.forgeslack.handler.ConfigurationHandler;
import com.derimagia.forgeslack.handler.FMLEventHandler;
import com.derimagia.forgeslack.handler.ForgeEventHandler;
import com.derimagia.forgeslack.slack.SlackRelay;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ForgeSlack.modId, name = ForgeSlack.modId, version = ForgeSlack.version, acceptableRemoteVersions = "*")
public class ForgeSlack {
    public static final String modId = "forgeslack";
    public static final String version = "0.2.0";

    public static Logger log = LogManager.getLogger(modId);
    private static SlackRelay slackRelay;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        if (ConfigurationHandler.enabled) {
            MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
            MinecraftForge.EVENT_BUS.register(new FMLEventHandler());
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (ConfigurationHandler.enabled) {
            slackRelay = SlackRelay.getInstance();
            slackRelay.startup();
        }
    }
  
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        if (ConfigurationHandler.enabled) {
            slackRelay.shutdown();
        }
    }
}