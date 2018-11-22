package com.messenger.client;

import com.messenger.common.GlobalSettings;
import com.messenger.common.Packet;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.Socket;
import java.security.InvalidParameterException;

class Client {

    private UserInterface ui;
    private Socket socket;
    private ServerConnection connection;

    Client() throws IOException {
        ui = new UserInterface();

        startSocket();
        startConnection();
    }

    void run() {
        connection.run();
    }

    private void packetHandler(Packet packet) {
        try {
            if (packet.getType() == Packet.Type.SYSTEM) {
                switch (SystemCode.fromValue(packet.getText())) {
                    case CLOSE:
                        connection.close();
                        return;
                    default:
                        throw new InvalidParameterException("Unsupported system code received");
                }
            }

            ui.printMessage(packet);
        } catch (Exception e) {
            errorHandler(e);
        }
    }

    private void errorHandler(Exception e) {
        // todo
        e.printStackTrace();
    }

    private void startConnection() throws IOException {
        connection = new ServerConnection(socket);
        connection.onPacket(this::packetHandler);
        connection.onError(this::errorHandler);

        System.out.println("Connection initialized");
    }

    private void startSocket() throws IOException {
        socket = new Socket(GlobalSettings.serverAddr, GlobalSettings.serverPort);
        socket.setSoTimeout(GlobalSettings.timeout);

        System.out.println("Socket opened");
    }

}
