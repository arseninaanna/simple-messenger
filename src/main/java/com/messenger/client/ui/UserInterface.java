package com.messenger.client.ui;

import com.messenger.common.Packet;

import java.io.*;
import java.util.function.Consumer;

public class UserInterface implements Runnable {

    private GuiMiddleware gui;

    public UserInterface() {
        gui = new GuiMiddleware();
    }

    public void run() {
        gui.init();
    }

    public void printSystemMessage(String msg) throws IOException {
        gui.printLine("!!! " + msg + " !!!");
    }

    public void printError(String msg) throws IOException {
        gui.printLine(msg);
    }

    public void printMessage(Packet msg) throws IOException {
        gui.printLine(msg.toString());
    }

    public void onMessageEnter(Consumer<String> fn) {
        gui.onMessageEnter(fn);
    }

    public void stop() {
        gui.close();
    }

}
