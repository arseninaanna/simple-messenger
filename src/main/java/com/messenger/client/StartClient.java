package com.messenger.client;

import java.io.IOException;

public class StartClient {

    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
