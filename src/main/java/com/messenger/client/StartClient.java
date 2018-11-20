package com.messenger.client;

import com.messenger.common.GlobalSettings;
import com.messenger.common.Packet;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;

public class StartClient {

    static boolean DEBUG = true;

    public static void main(String[] args) throws IOException {
        UserInterface ui = new UserInterface();

        try {
            Socket socket = new Socket(GlobalSettings.serverAddr, GlobalSettings.serverPort);
            socket.setSoTimeout(GlobalSettings.timeout);
            System.out.println("Connected");

            ServerConnection connection = new ServerConnection(socket);
            connection.onPacket(packet -> {
                try {
                    if (packet.getType() == Packet.Type.SYSTEM) {
                        SystemCode code = Enum.valueOf(SystemCode.class, packet.getText());
                        switch (code) {
                            case CLOSE:
                                connection.close();
                                return;
                            default:
                                throw new InvalidParameterException("Unsupported system code received");
                        }
                    }

                    ui.printMessage(packet);
                } catch (IOException e) {
                    // todo
                    e.printStackTrace();
                } catch (InvalidParameterException e) {
                    // todo
                    e.printStackTrace();
                }
            });
            connection.onError(e -> {
                // todo
                e.printStackTrace();
            });

            connection.run();

            System.out.println("End");
        } catch (ConnectException e) {
            ui.printError("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            ui.printError("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
