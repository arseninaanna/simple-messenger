package com.messenger.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that represents one data object, sent between client and server
 */
public class Packet {

    public enum Type {
        SYSTEM,
        SERVER,
        MESSAGE;

        private byte code;

        Type() {
            this.code = (byte) ordinal();
        }

        public byte code() {
            return code;
        }

        public static Type fromValue(int value) throws IllegalArgumentException {
            try {
                return Type.values()[value];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Unknown enum value: " + value);
            }
        }
    }

    /**
     * When server got this packet (in milliseconds)
     */
    private long timestamp;
    /**
     * Type of packet
     */
    private Type type;

    /**
     * Name of entity that has sent packet
     */
    private String emitter;
    /**
     * Text of packet
     */
    private String text;

    public Packet(Type type, String text, String emitter, long timestamp) {
        this.type = type;
        this.text = text;
        this.emitter = emitter;
        this.timestamp = timestamp;
    }

    public Packet(SystemCode code) {
        this(Type.SYSTEM, code.name(), "");
    }

    public Packet(Type type, String text) {
        this(type, text, "");
    }

    public Packet(Type type, String text, String nick) {
        this(type, text, nick, System.currentTimeMillis());
    }

    public String getText() {
        return text;
    }

    public String getEmitter() {
        if (type == Type.SERVER && emitter.length() == 0) {
            return "SERVER";
        }

        return emitter;
    }

    public Type getType() {
        return type;
    }

    public String getDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(timestamp));
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isCommand() {
        return text.startsWith("/");
    }

    @Override
    public String toString() {
        String content = getEmitter() + ": " + getText();

        return "[" + getDate() + "] " + content;
    }
}
