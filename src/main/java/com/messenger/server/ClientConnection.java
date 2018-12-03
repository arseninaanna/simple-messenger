package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;
import com.messenger.common.SystemCode;

import java.io.IOException;
import java.net.Socket;

class ClientConnection extends SocketWrapper {

    private String nickname;

    ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    String getNickname() {
        return nickname;
    }

    void setNickname(String nick) {
        nickname = nick;
    }

    @Override
    protected void handlePacket(Packet p) {
        if (!p.getEmitter().equals(nickname)) {
            sendPacket(p.respond("Invalid nick"));
            return;
        }

        super.handlePacket(p);
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
