package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.slack.SlackSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;


/**
 * @author derimagia
 */
public class ForgeEventHandler {

    @SubscribeEvent
    public void serverChat(ServerChatEvent event) {
        SlackSender.getInstance().send(event.getMessage(), event.getUsername());
    }

}