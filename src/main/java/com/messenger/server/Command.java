package com.messenger.server;

import com.messenger.common.Packet;

import java.security.InvalidParameterException;

public class Command {

    SocketClient socketClient;
    private Packet packet;

    private String command;
    private String[] params;

    Command(Packet packet, SocketClient client) {
        if (!packet.isCommand()) {
            throw new InvalidParameterException("Supplied packet is not a command");
        }

        this.socketClient = client;
        this.packet = packet;

        parsePacketText();
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }

    private void parsePacketText() {
        String[] parts = packet.getText().substring(1).trim().split("\\w+");

        command = "";
        params = new String[0];

    }

}
