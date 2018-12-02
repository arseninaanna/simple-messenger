package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.Socket;

class ClientConnection extends SocketWrapper {

    public String nickname;

    ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void close() throws IOException {
        sendPacket(new Packet(SystemCode.CLOSE));

        super.close();
    }

    void drop() throws IOException {
        super.close();
    }

}
