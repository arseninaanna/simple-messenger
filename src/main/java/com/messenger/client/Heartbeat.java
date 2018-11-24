package com.messenger.client;

import com.messenger.common.GlobalSettings;
import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;
import com.messenger.common.SystemCode;

public class Heartbeat extends Thread {

    private SocketWrapper socket;
    private int interval;

    Heartbeat(SocketWrapper socket) {
        this.socket = socket;
        this.interval = GlobalSettings.timeout / 2;
    }

    @Override
    public void run() {
        try {
            while (socket.isActive()) {
                socket.sendPacket(getPacket());
                System.out.println("Heartbeat sent");

                // Let the thread sleep for a while.
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
    }

    private Packet getPacket() {
        return new Packet(SystemCode.PING);
    }

}
