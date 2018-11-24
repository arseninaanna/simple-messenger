package com.messenger.client;

import com.messenger.common.GlobalSettings;
import com.messenger.common.Packet;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.Socket;
import java.security.InvalidParameterException;

class Client {

    UserInterface ui;
    private Socket socket;
    private ServerConnection connection;

    Client() {
        ui = new UserInterface();
    }

    void connect() throws IOException {
        startSocket();
        startConnection();
    }

    void run() {
        (new Heartbeat(connection)).start();

        connection.run();
    }

    private void socketPacketHandler(Packet packet) {
        try {
            if (packet.getType() == Packet.Type.SYSTEM) {
                switch (SystemCode.fromValue(packet.getText())) {
                    case CLOSE:
                        connection.close();
                        return;
                    case PONG:
                        System.out.println("pong received");
                        return;
                    default:
                        throw new InvalidParameterException("Unsupported system code received");
                }
            }

            ui.printMessage(packet);
        } catch (Exception e) {
            socketErrorHandler(e);
        }
    }

    private void socketErrorHandler(Exception e) {
        // todo
        e.printStackTrace();
    }

    private void startConnection() throws IOException {
        connection = new ServerConnection(socket);
        connection.onPacket(this::socketPacketHandler);
        connection.onError(this::socketErrorHandler);

        System.out.println("Connection initialized");
    }

    private void startSocket() throws IOException {
        socket = new Socket(GlobalSettings.serverAddr, GlobalSettings.serverPort);
        socket.setSoTimeout(GlobalSettings.timeout);

        System.out.println("Socket opened");
    }

}
