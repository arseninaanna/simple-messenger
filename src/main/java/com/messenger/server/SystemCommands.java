package com.messenger.server;

public class SystemCommands {

    public static String quit(Server server, SocketClient client, String[] argv) {
        return "close connection";
    }

    public static String help(Server server, SocketClient client, String[] argv) {
        return "help response for command";
    }

    public static String commands(Server server, SocketClient client, String[] argv) {
        return "commands list";
    }

    public static String stop(Server server, SocketClient client, String[] argv) {
        return "commands list";
    }

}
