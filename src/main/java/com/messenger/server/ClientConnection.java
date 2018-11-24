package com.messenger.server;

import com.messenger.common.SocketWrapper;

import java.io.IOException;
import java.net.Socket;

class ClientConnection extends SocketWrapper {

    ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

}
