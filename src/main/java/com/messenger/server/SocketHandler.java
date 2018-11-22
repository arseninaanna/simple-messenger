package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.PacketSerializer;

import java.io.IOException;
import java.net.Socket;

public class SocketHandler extends Thread {

    private int id;
    private Socket socket;

    SocketHandler(Socket socket) {
        this((int) (Math.random() * Integer.MAX_VALUE), socket);
    }

    SocketHandler(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            Packet p = new Packet(Packet.Type.MESSAGE, "test msg");
            try {
                byte[] pb = PacketSerializer.serizalize(p);
                socket.getOutputStream().write(pb);
                socket.getOutputStream().flush();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            System.out.println("wrote");

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getClientId() {
        return id;
    }
}
