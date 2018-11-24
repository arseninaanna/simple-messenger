package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.Socket;
import java.security.InvalidParameterException;

public class SocketHandler extends Thread {

    private Server server;

    private int clientId;
    private Socket socket;

    private ClientConnection wrapper;

    SocketHandler(Server server, Socket socket) {
        this(server, socket, (int) (Math.random() * Integer.MAX_VALUE));
    }

    SocketHandler(Server server, Socket socket, int clientId) {
        this.server = server;

        this.clientId = clientId;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            wrapper = new ClientConnection(socket);
            wrapper.onPacket(this::socketPacketHandler);
            wrapper.onError(this::socketErrorHandler);

            wrapper.run();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void socketPacketHandler(Packet packet) {
        try {
            System.out.println("New message: " + packet.getText());

            if (packet.getType() == Packet.Type.SYSTEM) {
                switch (SystemCode.fromValue(packet.getText())) {
                    case PING:
                        wrapper.sendPacket(new Packet(SystemCode.PONG));
                        return;
                    default:
                        throw new InvalidParameterException("Unsupported system code received");
                }
            }

            server.broadcast(packet);
        } catch (Exception e) {
            socketErrorHandler(e);
        }
    }

    private void socketErrorHandler(Exception e) {
        // todo
        try {
            wrapper.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();
    }

    ClientConnection getWrapper() {
        return wrapper;
    }

    int getClientId() {
        return clientId;
    }

}
