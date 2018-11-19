package com.messenger.client;

import com.messenger.common.GlobalSettings;

import javax.sound.midi.Track;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StartClient {

    static boolean DEBUG = true;

    public static void main(String[] args) throws IOException {
        UserInterface ui = new UserInterface();

        try {
            Socket soc = new Socket(GlobalSettings.serverAddr, GlobalSettings.serverPort);
            soc.setSoTimeout(GlobalSettings.timeout);
            System.out.println("Connected");

            BufferedInputStream socIn = new BufferedInputStream(soc.getInputStream());
            BufferedOutputStream socOut = new BufferedOutputStream(soc.getOutputStream());

            Scanner sc = new Scanner(socIn);
            while (!soc.isInputShutdown()) {
                System.out.println(socIn.read());
            }

//            String login = ui.promptForInput("Login: ");
//            ui.printSystemMessage("l - " + login);
        } catch (ConnectException e) {
            ui.printError("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            ui.printError("Failed to find host. Please, check the address.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
