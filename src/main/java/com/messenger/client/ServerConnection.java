package com.messenger.client;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;

import java.io.IOException;
import java.net.Socket;

class ServerConnection extends SocketWrapper {

    ServerConnection(Socket socket) throws IOException {
        super(socket);

        this.sendPacket(new Packet(Packet.Type.MESSAGE, "ssfdsfdsfdfdsfd"));
    }

}
