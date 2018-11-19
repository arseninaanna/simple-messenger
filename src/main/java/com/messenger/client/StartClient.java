package com.messenger.client;

import com.messenger.common.GlobalSettings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class StartClient {

    static boolean DEBUG = true;

    public static void main(String[] args) throws IOException {
        UserInterface ui = new UserInterface();

        try {
            Socket soc = new Socket(GlobalSettings.serverAddr, GlobalSettings.serverPort);
            soc.setSoTimeout(GlobalSettings.timeout);

            BufferedInputStream socIn = new BufferedInputStream(soc.getInputStream());
            BufferedOutputStream socOut = new BufferedOutputStream(soc.getOutputStream());

            System.out.println("test");
            String login = ui.promptForInput("Login: ");
            ui.printLine("l - " + login);
        } catch (ConnectException e) {
            ui.printLine("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            ui.printLine("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
