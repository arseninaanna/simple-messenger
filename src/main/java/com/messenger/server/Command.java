package com.messenger.server;

import com.messenger.common.Packet;

public class Command extends Packet {

    SocketClient socketClient;

    Command(Packet.Type type, String text, String emitter, long timestamp) {
        super(type, text, emitter, timestamp);
    }

    public String getCommand() {
        return ""; // todo
    }

    public String getParams() {
        return ""; // todo
    }

}
