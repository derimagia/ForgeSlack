package com.derimagia.forgeslack.slack;

import com.derimagia.forgeslack.ForgeSlack;
import com.derimagia.forgeslack.handler.ConfigurationHandler;
import org.eclipse.jetty.server.Server;

/**
 * @author derimagia
 */
public class SlackReceiveServer {
    private Server server = null;

    public SlackReceiveServer() {
        server = new Server(ConfigurationHandler.jettyServerPort);
        server.setHandler(new SlackReceiveHandler());

        try {
            server.start();
        } catch (Exception e) {
            ForgeSlack.log.error("Error starting ForgeSlack server: " + e);
        }
    }
}