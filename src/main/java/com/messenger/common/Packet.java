package com.messenger.common;

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
     * When server got this packet
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

    public Packet(Type type, String text, String nick) {
        this(type, text, nick, System.currentTimeMillis());
    }

    public String getText() {
        return text;
    }

    public String getEmitter() {
        return emitter;
    }

    public Type getType() {
        return type;
    }

    public Date getDate() {
        return new Date(timestamp);
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
        String content = emitter + ": " + text;

        return "[" + getDate() + "] " + content;
    }
}
