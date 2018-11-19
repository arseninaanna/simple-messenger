package com.messenger.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private int port;
    private boolean running = false;

    private ServerSocket sSocket;

    private int maxId = 0;
    private Map<Integer, SocketHandler> connections;

    Server(int port) throws IOException {
        this.port = port;

        sSocket = new ServerSocket(this.port);
        connections = new HashMap<>();
    }

    public void run() throws IOException {
        running = true;

        while (running) {
            Socket cSocket = sSocket.accept();

            // Process new client
            SocketHandler conn = new SocketHandler(maxId, cSocket);
            connections.put(maxId, conn);

            System.out.println("New socket#" + conn.getId());

            // Keep ID unique
            maxId++;
        }
    }

    public void stop() throws IOException {
        running = false;
        sSocket.close();
    }

}
