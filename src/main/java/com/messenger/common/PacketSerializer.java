package com.messenger.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Scanner;

/**
 * Class for client-server messages conversion
 * Currently we use signed values to store data
 * <p>
 * Structure:
 * 4 byte - total length
 * 1 byte - type
 * 8 byte - timestamp
 * 1 byte - emitter length (n)
 * n byte - emitter
 * 2 byte - text length (m)
 * m byte - text
 * </p>
 */
public class PacketSerializer {

    // total length + type length + timestamp length
    private static final int PREFIX_SIZE = Long.BYTES + Byte.BYTES + Long.BYTES;

    // PREFIX_SIZE + emitter length + text length
    public static final int MIN_SIZE = PREFIX_SIZE + Byte.BYTES + Short.BYTES;
    // MIN_SIZE + max emitter len + max text len
    public static final int MAX_SIZE = MIN_SIZE + Byte.MAX_VALUE + Short.MAX_VALUE;

    private static final String ENCODING = "UTF-8";

    /**
     * @param data - Packet to serialize
     * @return - byte stream of serialized data
     */
    public static byte[] serizalize(Packet data) throws InvalidParameterException, UnsupportedEncodingException {
        byte[] emitter = data.getEmitter().getBytes(ENCODING);
        byte[] text = data.getText().getBytes(ENCODING);

        if (emitter.length > Byte.MAX_VALUE) {
            throw new InvalidParameterException("Emitter length exceeded");
        }
        if (text.length > Short.MAX_VALUE) {
            throw new InvalidParameterException("Text length exceeded");
        }

        int size = MIN_SIZE + emitter.length + text.length;

        ByteBuffer buffer = ByteBuffer.allocate(size)
                .putInt(size)
                .put(data.getType().code())
                .putLong(data.getTimestamp())
                .put((byte) emitter.length)
                .put(emitter)
                .putShort((short) text.length)
                .put(text);

        return buffer.array();
    }

    /**
     * @param data - Byte array of serialized packet
     * @return - Deserialized packet object
     */
    public static Packet deserialize(byte[] data) throws InvalidParameterException, IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream dstream = new DataInputStream(stream);

        int totalLen = dstream.readInt();
        if (data.length < totalLen) {
            throw new InvalidParameterException("Data should be " + totalLen + " bytes long, " + data.length + " received");
        }

        byte type = dstream.readByte();
        long tstamp = dstream.readLong();

        byte emitterLen = dstream.readByte();
        String nick = readStr(dstream, emitterLen);

        short textLen = dstream.readShort();
        String text = readStr(dstream, textLen);

        return new Packet(Packet.Type.fromValue(type), text, nick, tstamp);
    }

    static void validatePacketLength(int len) throws InvalidParameterException {
        if (len < MIN_SIZE) {
            throw new InvalidParameterException("Packet length should be minimum " + MIN_SIZE + ", " + len + " received");
        }
        if (len > MAX_SIZE) {
            throw new InvalidParameterException("Packet length should be maximum " + MAX_SIZE + ", " + len + " received");
        }
    }

    private static String readStr(DataInputStream dstream, int length) throws IOException {
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = dstream.readByte();
        }

        return new String(b, ENCODING);
    }

}
