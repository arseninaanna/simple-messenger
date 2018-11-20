package com.messenger.client;

import com.messenger.common.GlobalSettings;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

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
                    ui.printMessage(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            connection.onError(error -> {
                // todo
                error.printStackTrace();
            });

            connection.run();
        } catch (ConnectException e) {
            ui.printError("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            ui.printError("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
