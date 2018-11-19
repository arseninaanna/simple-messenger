package com.messenger.server;

import java.io.IOException;
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

    public void run() throws IOException {
        while (!socket.isClosed()) {
            socket.getOutputStream().write("test".getBytes());
            socket.getOutputStream().flush();
            System.out.println("wrote");

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }
}
