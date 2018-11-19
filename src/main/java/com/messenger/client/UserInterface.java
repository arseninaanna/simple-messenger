package com.messenger.client;

import com.messenger.common.Notification;

import java.io.*;

public class UserInterface {

    private Console cons;

    String currentPrompt;

    UserInterface() {
        cons = new Console();
    }

    public String promptForInput(String msg) throws IOException {
        cons.print(msg);

        return cons.readLine();
    }

    public void printSystemMessage(String msg) throws IOException {
        cons.printLine("!!! " + msg + " !!!");
    }

    public void printMessage(Notification msg) throws IOException {
        cons.printLine(msg.toString());
    }

    public void onMessageEnter() {
        // noop
    }

}
