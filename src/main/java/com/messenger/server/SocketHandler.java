package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidParameterException;

public class SocketHandler implements Runnable {

    private Server server;

    private int clientId;
    private Socket socket;
    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private ClientConnection wrapper;

    SocketHandler(Server server, Socket socket) {
        this(server, socket, (int) (Math.random() * Integer.MAX_VALUE));
    }

    SocketHandler(Server server, Socket socket, int clientId) {
        this.server = server;

        this.clientId = clientId;
        this.socket = socket;
    }

    public void run() {
        try {
            wrapper = new ClientConnection(socket);
            wrapper.setConnectionId(clientId);

            wrapper.onPacket(this::socketPacketHandler);
            wrapper.onError(this::socketErrorHandler);

            wrapper.run(); // stops only on error or socket close
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.error("Unable to close server connection error: {}", e1.toString());
            }
            e.printStackTrace();
        }
    }

    private void socketPacketHandler(Packet packet) {
        try {
            System.out.println("New message: " + packet.getText());

            if (packet.hasType(Packet.Type.SYSTEM)) {
                switch (SystemCode.fromValue(packet.getText())) {
                    case PING:
                        logger.info("PING packet was received from user \"{}\"", wrapper.getNickname());
                        wrapper.sendPacket(new Packet(SystemCode.PONG));
                        return;
                    default:
                        throw new InvalidParameterException("Unsupported system code received");
                }
            }
            if (packet.hasType(Packet.Type.MESSAGE)) {
                if (packet.isCommand()) {
                    server.execute(new Command(packet, wrapper));
                    return;
                }
                if (packet.getEmitter().equals("")) {
                    wrapper.sendPacket(packet.respond("Authentication is required"));
                    return;
                }

                server.broadcast(packet);
                return;
            }

            wrapper.sendPacket(packet.respond("Invalid packet type"));
        } catch (Exception e) {
            socketErrorHandler(e);
        }
    }

    private void socketErrorHandler(Exception e) {
        if (e instanceof IOException && !(e instanceof SocketException)) {
            try {
                wrapper.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e instanceof SocketException) {
            try {
                wrapper.drop();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        server.removeConnection(clientId);

        e.printStackTrace();
    }

    ClientConnection getWrapper() {
        return wrapper;
    }

    int getClientId() {
        return clientId;
    }

}
