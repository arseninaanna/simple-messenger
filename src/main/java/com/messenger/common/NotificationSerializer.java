package com.messenger.common;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Scanner;

/**
 * Class for client-server messages conversion
 */
public class NotificationSerializer {

    private static final int PREFIX_SIZE = Byte.BYTES + Long.BYTES;
    private static final int MIN_SIZE = PREFIX_SIZE + 2; // 2 for newline
    private static final String ENCODING = "UTF-8";

    /**
     * @param data - Notification to serialize
     * @return - byte stream of serialized data
     */
    public byte[] serizalize(Notification data) throws UnsupportedEncodingException {
        byte[] emitter = (data.getEmitter() + "\n").getBytes(ENCODING);
        byte[] text = data.getEmitter().getBytes(ENCODING);

        int size = PREFIX_SIZE + emitter.length + text.length;

        ByteBuffer buffer = ByteBuffer.allocate(size)
                .put(data.getType().code())
                .putLong(data.getTimestamp())
                .put(emitter)
                .put(text);

        return buffer.array();
    }

    /**
     * @param data - Byte array of serialized notification
     * @return - Deserialized notification object
     */
    public Notification deserialize(byte[] data) throws InvalidParameterException {
        if (data.length < MIN_SIZE) {
            throw new InvalidParameterException("Data should be minimum " + MIN_SIZE + " bytes long, " + data.length + " got");
        }
        Scanner sc = new Scanner(new ByteArrayInputStream(data), ENCODING);

        byte type = sc.nextByte();
        long tstamp = sc.nextLong();
        String nick = sc.nextLine();

        sc.useDelimiter("\\A"); // we need that too read rest of the stream
        String text = sc.hasNext() ? sc.next() : "";

        return new Notification(Notification.Type.fromValue(type), text, nick, tstamp);
    }

}
