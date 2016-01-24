package com.derimagia.forgeslack.slack;

import com.derimagia.forgeslack.ForgeSlack;
import com.derimagia.forgeslack.handler.ConfigurationHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author derimagia
 */
public class SlackReceiveHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getMethod().equals("POST")) {
            String username = request.getParameter("user_name");
            String text = request.getParameter("text");
            String message = String.format("[Slack] <%s> %s", username, text);
            String token = request.getParameter("token");

            if (!token.isEmpty() && token.equals(ConfigurationHandler.slackToken)) {
                if (!username.isEmpty() && !(username.trim().equals("slackbot"))) {
                    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
                }
            } else {
                ForgeSlack.log.error("Token on Slack Outgoing WebHook is invalid! Ignoring Request.");
            }

        }
    }
}