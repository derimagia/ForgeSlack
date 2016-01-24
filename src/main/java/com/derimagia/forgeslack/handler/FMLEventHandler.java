package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.slack.SlackSender;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * @author derimagia
 */
public class FMLEventHandler {
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // @TODO: Localize this?
        SlackSender.getInstance().send("[Joined the Game]", event.player.getDisplayName());
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // @TODO: Localize this?
        SlackSender.getInstance().send("[Left the Game]", event.player.getDisplayName());
    }
}
