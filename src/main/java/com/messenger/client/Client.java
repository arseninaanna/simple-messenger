package com.messenger.client;

import com.messenger.client.ui.UserInterface;
import com.messenger.common.GlobalSettings;
import com.messenger.common.Packet;
import com.messenger.common.SystemCode;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;

class Client {

    private UserInterface ui;
    private Socket socket;
    private ServerConnection connection;

    private String nickname;

    Client() {
        ui = new UserInterface();
    }

    void run() {
        ui.onNickEnter(n -> {
            if(!hasConnection()) {
                connect();
            }

            Packet p = makeMessage("/auth " + n);
            connection.sendPacket(p, (response) -> {
                if (response.getText().equals("OK")) {
                    System.out.println("Authorized");
                    nickname = n;
                    ui.connected(n);
                } else {
                    ui.quieted();
                    ui.printSystemMessage("Nick `" + n + "` is occupied");
                }

                return false;
            });
        });
        ui.onMessageEnter(m -> connection.sendPacket(makeMessage(m)));

        ui.onQuit(() -> disconnect(() -> ui.quieted()));
        ui.onClose(() -> disconnect(() -> ui.stop()));

        ui.run();
    }

    private void connect() {
        try {
            startSocket();
            startConnection();
        } catch (ConnectException e) {
            ui.fatalError("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            ui.fatalError("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        (new Heartbeat(connection)).start();
        (new Thread(connection)).start();
    }

    private void disconnect(Runnable cb) {
        Packet p = makeMessage("/quit");

        if (hasConnection()) {
            connection.sendPacket(p, (response) -> {
                System.out.println("quit");
                cb.run();
                nickname = null;

                return true;
            });
        } else {
            System.out.println("No connection instance");
            cb.run();
            nickname = null;
        }
    }

    private void disconnect() {
        disconnect(() -> {
        });
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
        if (e instanceof EOFException) {
            disconnect();
            ui.quieted();

            try {
                connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e instanceof SocketException) {
            ui.quieted();

            try {
                connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

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

    private boolean hasConnection() {
        return connection != null && connection.isActive();
    }

    private Packet makeMessage(String text) {
        return new Packet(Packet.Type.MESSAGE, text, nickname);
    }

}
