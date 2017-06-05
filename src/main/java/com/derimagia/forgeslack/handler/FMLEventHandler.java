package com.derimagia.forgeslack.handler;

import com.derimagia.forgeslack.slack.SlackRelay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author derimagia
 */
public class FMLEventHandler {
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // @TODO: Localize this?
        SlackRelay.getInstance().sendMessage("_[Joined the Game]_", getName(event.player));
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // @TODO: Localize this?
        SlackRelay.getInstance().sendMessage("_[Left the Game]_", getName(event.player));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            SlackRelay.getInstance().sendMessage("_" + ((EntityPlayer) event.getEntity()).getCombatTracker().getDeathMessage().getUnformattedText() + "_", getName((EntityPlayer) event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onPlayerRecieveAchievement(AchievementEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            if (((EntityPlayerMP) event.getEntity()).getStatFile().hasAchievementUnlocked(event.getAchievement())) {
                return;
            }
            if (!((EntityPlayerMP) event.getEntity()).getStatFile().canUnlockAchievement(event.getAchievement())) {
                return;
            }

            ITextComponent achievementComponent = event.getAchievement().getStatName();
            ITextComponent achievementText      = new TextComponentString("[").appendSibling(achievementComponent).appendText("]");

            String playerName = getName(event.getEntityPlayer());
            SlackRelay.getInstance().sendMessage("_" + playerName + " has earned the achievement: " + achievementText.getUnformattedText() + "_", playerName);
        }
    }

    private static String getName(EntityPlayer player) {
        return ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName().getUnformattedText());
    }
}
