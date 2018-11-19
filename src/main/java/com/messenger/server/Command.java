package com.messenger.server;

import com.messenger.common.Notification;

public class Command extends Notification {

    SocketClient socketClient;

    Command(Notification.Type type, String text, String emitter, long timestamp) {
        super(type, text, emitter, timestamp);
    }

    public String getCommand() {
        return ""; // todo
    }

    public String getParams() {
        return ""; // todo
    }

}
