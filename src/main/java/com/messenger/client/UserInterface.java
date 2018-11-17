package com.messenger.client;

import com.messenger.common.Notification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;

public class UserInterface {

    BufferedReader in;
    BufferedWriter out;

    String currentPrompt;

    UserInterface() {
        Console console = System.console();

        in = new BufferedReader(console.reader());
        out = new BufferedWriter(console.writer());
    }

    public String promptForInput(String msg) throws IOException {
        out.write(msg);
        String input = in.readLine();

        return input;
    }

    public void printSystemMessage(String msg) throws IOException {
        printLine("!!! " + msg + " !!!");
    }

    public void printMessage(Notification msg) {
        // noop
    }

    private void printLine(String text) throws IOException {
        out.write(text);
        out.newLine();
    }

    public void onMessageEnter() {
        // noop
    }

}
