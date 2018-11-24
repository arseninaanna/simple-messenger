package com.messenger.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private int port;

    private ServerSocket sSocket;

    private int maxId = 0;
    private Map<Integer, SocketHandler> connections;

    Server(int port) {
        this.port = port;
        this.connections = new HashMap<>();
    }

    public void run() throws IOException {
        sSocket = new ServerSocket(this.port);

        while (!sSocket.isClosed()) {
            Socket cSocket = sSocket.accept();

            // Process new client
            SocketHandler conn = new SocketHandler(maxId, cSocket);

            System.out.println("User#" + conn.getClientId() + " connected");
            connections.put(maxId, conn);

            conn.start();

            // Keep ID unique
            maxId++;
        }
    }

    public void stop() throws IOException {
        sSocket.close();
    }

}
