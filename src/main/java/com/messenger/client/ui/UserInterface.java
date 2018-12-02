package com.messenger.client.ui;

import com.messenger.common.Packet;

import java.util.function.Consumer;

public class UserInterface implements Runnable {

    private GuiMiddleware gui;

    public UserInterface() {
        gui = new GuiMiddleware();
    }

    public void run() {
        gui.init();
    }

    public void stop() {
        gui.dispose();
    }

    public void printSystemMessage(String msg) {
        gui.printMessage("!!! " + msg + " !!!");
    }

    public void printMessage(Packet msg) {
        gui.printMessage(msg.toString());
    }

    public void showError(String msg) {
        gui.showModal("Error: " + msg);
    }

    public void fatalError(String msg) {
        gui.printMessage("Fatal error: " + msg);
        gui.block();
    }

    public void connected(String nick) {
        gui.connected(nick);
    }

    public void quieted() {
        gui.quieted();
    }

    public void onMessageEnter(Consumer<String> fn) {
        gui.onMessageEnter(fn);
    }

    public void onNickEnter(Consumer<String> fn) {
        gui.onNickEnter(fn);
    }

    public void onQuit(Runnable fn) {
        gui.onQuit(fn);
    }

    public void onClose(Runnable fn) {
        gui.onClose(fn);
    }

}
