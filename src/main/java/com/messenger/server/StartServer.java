package com.messenger.server;

import com.messenger.common.GlobalSettings;

import java.io.IOException;

public class StartServer {

    public static void main(String[] args) {
        try {
            Server server = new Server(GlobalSettings.serverPort);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
