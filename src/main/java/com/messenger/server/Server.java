package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

class Server {

    private int port;
    private ServerSocket sSocket;
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private CommandRegistry commands;

    private final static int MESSAGES_LOG = 10;
    private ConcurrentLinkedQueue<Packet> lastMessages;

    private int maxClientId = 0;
    private ConcurrentMap<Integer, SocketHandler> connections;

    Server(int port) {
        logger.info("Server initialization has started");
        this.port = port;
        this.connections = new ConcurrentHashMap<>();
        this.commands = new CommandRegistry("./plugins");

        lastMessages = new ConcurrentLinkedQueue<>();

        logger.info("Server initialization has finished");
    }

    void run() throws IOException {
        sSocket = new ServerSocket(this.port);

        while (!sSocket.isClosed()) {
            Socket cSocket = sSocket.accept();
            logger.info("Server is running");

            // Process new client
            SocketHandler conn = new SocketHandler(this, cSocket, maxClientId);
            connections.put(maxClientId, conn);

            logger.info("User#" + conn.getClientId() + " connected");

            (new Thread(conn)).start();

            // Keep ID unique
            maxClientId++;
        }

        logger.info("Server shut down");
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

    void execute(Command c) {
        logger.info("Executing \"{} {}\" command", c.getCommand(), Arrays.toString(c.getParams()));

        new Thread(() -> {
            String response = commands.execute(this, c);

            if (response == null) {
                c.getConnection().sendPacket(c.getPacket().respond("Command not found"));
            } else if (response.length() > 0) {
                c.getConnection().sendPacket(c.getPacket().respond(response));
            }
        }).start();
    }

    public Packet getGreeting(ClientConnection client) {
        return new Packet(Packet.Type.SERVER, "Welcome to test chat!");
    }

    public Packet[] getLastMessages() {
        Packet[] p = new Packet[lastMessages.size()];

        return lastMessages.toArray(p);
    }

    public void removeConnection(int clientId) {
        connections.remove(clientId);
    }

    public List<ClientConnection> getConnections() {
        LinkedList<ClientConnection> list = new LinkedList<>();

        for (ConcurrentMap.Entry<Integer, SocketHandler> entry : connections.entrySet()) {
            list.add(entry.getValue().getWrapper());
        }

        return list;
    }

    void stop() throws IOException {
        // TODO: broadcast system packet with close command
        sSocket.close();
    }

}
