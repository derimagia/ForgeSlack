package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.ForgeSlack;
import com.derimagia.forgeslack.slack.SlackRelay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.text.MessageFormat;

public class ForgeEventHandler {
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        ForgeSlack.getSlackRelay().sendMessage(event.getMessage(), event.getPlayer());
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // @TODO: Localize this?
        ForgeSlack.getSlackRelay().sendMessage("_[Joined the Game]_", event.player);
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // @TODO: Localize this?
        ForgeSlack.getSlackRelay().sendMessage("_[Left the Game]_", event.player);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer  && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote) {
            ForgeSlack.getSlackRelay().sendMessage("_" + ((EntityPlayer) event.getEntity()).getCombatTracker().getDeathMessage().getUnformattedText() + "_", (EntityPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void onPlayerReceiveAdvancement(AdvancementEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote)) {
            return;
        }

        if (!(event.getAdvancement().getDisplay() != null && event.getAdvancement().getDisplay().shouldAnnounceToChat())) {
            return;
        }

        String achievementText = event.getAdvancement().getDisplayText().getUnformattedText();
        EntityPlayer player = event.getEntityPlayer();
        String playerName = getName(player);
        String msg = MessageFormat.format("_{0} has earned the achievement: {1}_", playerName, achievementText);

        ForgeSlack.getSlackRelay().sendMessage(msg, player);
    }

    private static String getName(EntityPlayer player) {
        return ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName().getUnformattedText());
    }
}
