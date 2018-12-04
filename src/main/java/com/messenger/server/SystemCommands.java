package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Predefined set of privileged commands, that have access to server and client objects
 */
public class SystemCommands {

    private static final Logger logger = LoggerFactory.getLogger(SystemCommands.class);

    public static String auth(Server server, ClientConnection client, Command command) {
        String nick = command.getParams()[0].trim();

        boolean occupied = false;
        for (ClientConnection c : server.getConnections()) {
            if (c.getNickname().equals(nick)) {
                occupied = true;
                break;
            }
        }
        if (occupied) {
            return "Nick is in use";
        }

        client.setNickname(nick);
        logger.info("Nickname \"{}\" to user with id {} was set", client.getNickname(), client.getConnectionId());

        client.sendPacket(command.getPacket().respond("OK"));
        client.sendPacket(server.getGreeting(client));

        for (Packet p : server.getLastMessages()) {
            client.sendPacket(p);
        }

        return null;
    }

    /**
     * Graceful quit from chat
     */
    public static String quit(Server server, ClientConnection client, Command command) {
        client.sendPacket(command.getPacket().respond(SystemCode.CLOSE));

        try {
            client.drop();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to close client connection");
        }

        return null;
    }

    /**
     * Show help information for some command
     * May use this solution: https://stackoverflow.com/a/21889648
     */
    public static String help(Server server, ClientConnection client, Command command) {
        return "help response for command";
    }

    /**
     * Shows available list of commands
     */
    public static String commands(Server server, ClientConnection client, Command command) {
        return "commands list";
    }

    /**
     * Stops the whole server. Requires simple password validation
     */
    public static String stop(Server server, ClientConnection client, String[] argv) {
        return "commands list";
    }

}
