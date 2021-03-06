package com.messenger.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PacketScanner {

    private DataInputStream inputStream;

    public PacketScanner(InputStream input) {
        this.inputStream = new DataInputStream(input);
    }

    public Packet nextPacket() throws IOException {
        // Read packet length
        int len = inputStream.readInt();
        PacketSerializer.validatePacketLength(len);

        byte[] b = new byte[len];

        // Push packet length into array, as it has already been read
        byte[] lenb = ByteBuffer.allocate(4).putInt(len).array();
        System.arraycopy(lenb, 0, b, 0, 4);

        // Read the rest of the packet
        for (int i = 4; i < len; i++) {
            b[i] = inputStream.readByte();
        }

        return PacketSerializer.deserialize(b);
    }

}
