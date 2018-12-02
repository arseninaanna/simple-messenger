package com.messenger.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class SocketWrapper implements Runnable {

    private Socket socket;

    private BufferedInputStream input;
    private BufferedOutputStream output;

    private Consumer<Packet> onPacket;
    private Consumer<Exception> onError;

    public SocketWrapper(Socket socket) throws IOException {
        this.socket = socket;

        input = new BufferedInputStream(socket.getInputStream());
        output = new BufferedOutputStream(socket.getOutputStream());
    }

    public void run() {
        PacketScanner sc = new PacketScanner(input);

        while (isActive()) {
            try {
                Packet p = sc.nextPacket();
                handlePacket(p);
            } catch (IOException e) {
                handleError(e);
            }
        }
    }

    public void onPacket(Consumer<Packet> fn) {
        onPacket = fn;
    }

    public void onError(Consumer<Exception> fn) {
        onError = fn;
    }

    protected void handlePacket(Packet p) {
        onPacket.accept(p);
    }

    protected void handleError(Exception e) {
        onError.accept(e);
    }

    public void sendPacket(Packet p) {
        try {
            byte[] b = PacketSerializer.serialize(p);

            output.write(b, 0, b.length);
            output.flush();
        } catch (IOException e) {
            handleError(e);
        }
    }

    public boolean isActive() {
        return !socket.isClosed();
    }

    public void close() throws IOException {
        socket.close();
    }

}
