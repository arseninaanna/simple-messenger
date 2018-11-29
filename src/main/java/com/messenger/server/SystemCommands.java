package com.messenger.server;

/**
 * Predefined set of privileged commands, that have access to server and client objects
 */
public class SystemCommands {

    /**
     * Graceful quit from chat
     */
    public static String quit(Server server, ClientConnection client, String[] argv) {
        return "close connection";
    }

    /**
     * Show help information for some command
     * May use this solution: https://stackoverflow.com/a/21889648
     */
    public static String help(Server server, ClientConnection client, String[] argv) {
        return "help response for command";
    }

    /**
     * Shows available list of commands
     */
    public static String commands(Server server, ClientConnection client, String[] argv) {
        return "commands list";
    }

    /**
     * Stops the whole server. Requires simple password validation
     */
    public static String stop(Server server, ClientConnection client, String[] argv) {
        return "commands list";
    }

}
