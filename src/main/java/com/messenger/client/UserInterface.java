package com.messenger.client;

import com.messenger.common.Packet;

import java.io.*;
import java.util.function.Consumer;

class UserInterface implements Runnable {

    private Console cons;

    private Consumer<String> onMessage;
    private boolean running;

    UserInterface() {
        cons = new Console();
    }

    public void run() {
        running = true;

        while (running) {
            try {
                System.out.println("Reading new input");
                String message = cons.readLine();

                if(message.length() > 0) {
                    System.out.println("Sending message");
                    onMessage.accept(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    void onMessageEnter(Consumer<String> fn) {
        onMessage = fn;
    }

    void stop() {
        running = false;
    }

}
