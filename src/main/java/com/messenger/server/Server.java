package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private int port;

    private ServerSocket sSocket;

    private int maxClientId = 0;
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
            SocketHandler conn = new SocketHandler(this, cSocket, maxClientId);

            System.out.println("User#" + conn.getClientId() + " connected");
            connections.put(maxClientId, conn);

            conn.start();

            // Keep ID unique
            maxClientId++;
        }
    }

    public void broadcast(Packet p) {
        for (Map.Entry<Integer, SocketHandler> entry : connections.entrySet()) {
            SocketWrapper wrapper = entry.getValue().getWrapper();
            if(wrapper.isActive()) {
                wrapper.sendPacket(p);
            }
        }
    }

    public void stop() throws IOException {
        // TODO: broadcast system packet with close command
        sSocket.close();
    }

}
