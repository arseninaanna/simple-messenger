package com.messenger.client;

import com.messenger.common.Packet;
import com.messenger.common.PacketScanner;
import com.messenger.common.PacketSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

class ServerConnection {

    private Socket socket;

    private BufferedInputStream input;
    private BufferedOutputStream output;

    private Consumer<Packet> onPacket;
    private Consumer<Exception> onError;

    ServerConnection(Socket socket) throws IOException {
        this.socket = socket;

        input = new BufferedInputStream(socket.getInputStream());
        output = new BufferedOutputStream(socket.getOutputStream());
    }

    void run() {
        PacketScanner sc = new PacketScanner(input);

        while (!socket.isClosed()) {
            try {
                Packet p = sc.nextPacket();
                onPacket.accept(p);
            } catch (IOException e) {
                onError.accept(e);
            }
        }
    }

    void onPacket(Consumer<Packet> fn) {
        onPacket = fn;
    }

    void onError(Consumer<Exception> fn) {
        onError = fn;
    }

    void sendPacket(Packet p) throws IOException {
        byte[] b = PacketSerializer.serizalize(p);

        output.write(b, 0, b.length);
        output.flush();
    }

    void close() throws IOException {
        socket.close();
    }

}
