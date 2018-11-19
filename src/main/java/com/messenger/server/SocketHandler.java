package com.messenger.server;

import java.net.Socket;

public class SocketHandler {

    private int id;
    private Socket socket;

    SocketHandler(Socket socket) {
        this((int) (Math.random() * Integer.MAX_VALUE), socket);
    }

    SocketHandler(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    public int getId() {
        return id;
    }
}
