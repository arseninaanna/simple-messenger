package com.messenger.server;

import com.messenger.common.Packet;
import com.messenger.common.SocketWrapper;
import com.messenger.common.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

class ClientConnection extends SocketWrapper {

    private int connectionId = -1;
    private String nickname = "";
    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    String getNickname() {
        return nickname;
    }

    void setNickname(String nick) {
        nickname = nick;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    protected void handlePacket(Packet p) {
        if (!p.getEmitter().equals("") && !p.getEmitter().equals(nickname)) {
            System.out.println("Got invalid nick; expected: " + nickname + "; received: " + p.getEmitter());
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
