package com.messenger.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class StartClient {

    public static final boolean DEBUG = true;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        try {
            client.connect();
            client.run();
        } catch (ConnectException e) {
            client.ui.printError("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            client.ui.printError("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
