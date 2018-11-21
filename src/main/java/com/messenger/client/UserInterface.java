package com.messenger.client;

import com.messenger.common.Packet;

import java.io.*;

// todo: add multithreading
class UserInterface {

    private Console cons;

    String currentPrompt;

    UserInterface() {
        cons = new Console();
    }

    String promptForInput(String msg) throws IOException {
        cons.print(msg);

        return cons.readLine();
    }

    void printSystemMessage(String msg) throws IOException {
        cons.printLine("!!! " + msg + " !!!");
    }

    void printError(String msg) throws IOException {
        cons.printLine(msg);
    }

    void printMessage(Packet msg) throws IOException {
        cons.printLine(msg.toString());
    }

    void onMessageEnter() {
        // noop
    }

}
