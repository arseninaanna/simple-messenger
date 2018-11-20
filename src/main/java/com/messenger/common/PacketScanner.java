package com.messenger.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PacketScanner {

    private DataInputStream inputStream;

    public PacketScanner(InputStream input) {
        this.inputStream = new DataInputStream(input);
    }

    public Packet nextPacket() throws IOException {
        int len = inputStream.readInt();
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = inputStream.readByte();
        }

        return PacketSerializer.deserialize(b);
    }

}
