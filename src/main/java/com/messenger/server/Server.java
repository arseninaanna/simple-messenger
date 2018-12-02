package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

class Server {

    private int port;

    private ServerSocket sSocket;

    private final static int MESSAGES_LOG = 10;
    private ConcurrentLinkedQueue<Packet> lastMessages;

    private int maxClientId = 0;
    private ConcurrentMap<Integer, SocketHandler> connections;

    Server(int port) {
        this.port = port;
        this.connections = new ConcurrentHashMap<>();

        lastMessages = new ConcurrentLinkedQueue<>();
    }

    void run() throws IOException {
        sSocket = new ServerSocket(this.port);

        while (!sSocket.isClosed()) {
            Socket cSocket = sSocket.accept();

            // Process new client
            SocketHandler conn = new SocketHandler(this, cSocket, maxClientId);
            connections.put(maxClientId, conn);

            System.out.println("User#" + conn.getClientId() + " connected");

            (new Thread(conn)).start();

            // Keep ID unique
            maxClientId++;
        }
    }

    void broadcast(Packet p) {
        if (p.getType() == Packet.Type.MESSAGE) {
            lastMessages.add(p);

            if (lastMessages.size() > MESSAGES_LOG) {
                lastMessages.poll();
            }
        }

        for (ConcurrentMap.Entry<Integer, SocketHandler> entry : connections.entrySet()) {
            SocketWrapper wrapper = entry.getValue().getWrapper();
            if (wrapper.isActive()) {
                wrapper.sendPacket(p);
            }
        }
    }

    public String getGreeting(ClientConnection client) {
        return "Welcome to test chat!";
    }

    void stop() throws IOException {
        // TODO: broadcast system packet with close command
        sSocket.close();
    }

}
