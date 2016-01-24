package com.derimagia.forgeslack;

import com.derimagia.forgeslack.handler.ConfigurationHandler;
import com.derimagia.forgeslack.handler.FMLEventHandler;
import com.derimagia.forgeslack.handler.ForgeEventHandler;
import com.derimagia.forgeslack.slack.SlackReceiveServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author derimagia
 */
@Mod(modid = ForgeSlack.modId, name = ForgeSlack.modId, version = ForgeSlack.version, acceptableRemoteVersions = "*")
public class ForgeSlack {

    public static final String modId = "ForgeSlack";
    public static final String version = "0.1.0";

    public static Logger log = LogManager.getLogger(modId);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        if (ConfigurationHandler.enabled) {
            MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
            FMLCommonHandler.instance().bus().register(new FMLEventHandler());
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (ConfigurationHandler.enabled) {
            new SlackReceiveServer();
        }
    }

}