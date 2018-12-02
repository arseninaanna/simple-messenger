package com.messenger.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that represents one data object, sent between client and server
 */
public class Packet {

    /**
     * Type of packet, can be encodes as integer
     */
    public enum Type {
        SYSTEM, // System packet contains only system codes, that can't be emitted directly by user
        SERVER, // Message from server itself, usually some announcements
        RESPONSE, // Some response of server command
        MESSAGE, // Message from another user
        ;

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
     * Contains local sender client packet id.
     * It is ok, if it would overflow, as we use it only for response detection
     */
    private short localId = -1;
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
        this.timestamp = timestamp;

        if (emitter == null) {
            this.emitter = "";
        } else {
            this.emitter = emitter;
        }
    }

    public Packet(SystemCode sysCode) {
        this(Type.SYSTEM, sysCode.code(), "");
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

    public boolean hasType(Type t) {
        return type == t;
    }

    public boolean hasType(Type[] tArr) {
        for (Type t : tArr) {
            if (type == t) {
                return true;
            }
        }

        return false;
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

    public void setId(short localId) {
        this.localId = localId;
    }

    public short getId() {
        return localId;
    }

    public Packet respond(String text) {
        Packet resp = new Packet(Type.RESPONSE, text);
        resp.setId(getId());

        return resp;
    }

    public Packet respond(SystemCode code) {
        Packet resp = new Packet(code);
        resp.setId(getId());

        return resp;
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
