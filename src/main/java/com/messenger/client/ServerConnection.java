package com.messenger.client;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

class ServerConnection extends SocketWrapper {

    private final static Packet.Type[] allowedCbTypes = new Packet.Type[]{Packet.Type.SYSTEM, Packet.Type.RESPONSE};

    private short localId = 0;
    private ConcurrentMap<Short, Function<Packet, Boolean>> callbacks;

    ServerConnection(Socket socket) throws IOException {
        super(socket);

        callbacks = new ConcurrentHashMap<>();
    }

    @Override
    protected void handlePacket(Packet p) {
        short id = p.getId();

        if (p.hasType(allowedCbTypes) && callbacks.containsKey(id)) {
            System.out.println("Call callback");
            boolean process = callbacks.get(id).apply(p);
            callbacks.remove(id);

            if (!process) {
                return;
            }
        }

        super.handlePacket(p);
    }

    @Override
    public void sendPacket(Packet p) {
        addId(p);

        super.sendPacket(p);
    }

    void sendPacket(Packet p, Function<Packet, Boolean> cb) {
        addId(p);
        addCallback(p, cb);
        System.out.println(p.getId() + " -> " + p.getText());

        super.sendPacket(p);
    }

    private void addCallback(Packet p, Function<Packet, Boolean> cb) {
        if (p.getId() != -1) {
            callbacks.put(p.getId(), cb);
        }
    }

    private void addId(Packet p) {
        p.setId(localId++);
    }

}
