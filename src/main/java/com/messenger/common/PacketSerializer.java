package com.messenger.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

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
    private static final int PREFIX_SIZE = Integer.BYTES + Byte.BYTES + Long.BYTES;

    // PREFIX_SIZE + emitter length + text length
    private static final int MIN_SIZE = PREFIX_SIZE + Byte.BYTES + Short.BYTES;
    // MIN_SIZE + max emitter len + max text len
    private static final int MAX_SIZE = MIN_SIZE + Byte.MAX_VALUE + Short.MAX_VALUE;

    private static final String ENCODING = "UTF-8"; // We always encode/decode utf-8 strings

    /**
     * @param data - Packet to serialize
     * @return - byte stream of serialized data
     */
    public static byte[] serialize(Packet data) throws InvalidParameterException, UnsupportedEncodingException {
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
        long timestamp = dstream.readLong();

        byte emitterLen = dstream.readByte();
        String nick = readStr(dstream, emitterLen, false);

        short textLen = dstream.readShort();
        String text = readStr(dstream, textLen, true);

        return new Packet(Packet.Type.fromValue(type), text, nick, timestamp);
    }

    static void validatePacketLength(int len) throws InvalidParameterException {
        if (len < MIN_SIZE) {
            throw new InvalidParameterException("Packet length should be minimum " + MIN_SIZE + ", " + len + " received");
        }
        if (len > MAX_SIZE) {
            throw new InvalidParameterException("Packet length should be maximum " + MAX_SIZE + ", " + len + " received");
        }
    }

    /**
     * Reads string of arbitrary length from input stream
     *
     * @param stream
     * @param length string length in bytes
     * @throws IOException
     */
    private static String readStr(InputStream stream, int length, boolean allowEof) throws IOException {
        byte[] b = new byte[length];
        int readBytes = stream.read(b);
        if (readBytes != length && !(allowEof && length == 0)) {
            throw new EOFException("Unexpected end of steam. Read bytes " + readBytes + "; required " + length);
        }

        return new String(b, ENCODING);
    }

}
