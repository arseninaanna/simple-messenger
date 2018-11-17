package com.messenger.common;

import java.util.Date;

/**
 * Class that represents one data object, sent between client and server
 */
public class Notification {

    public enum Type {
        SYSTEM,
        SERVER,
        MESSAGE,
    }

    /**
     * When server got this notification
     */
    private long timestamp;
    /**
     * Type of notification
     */
    private Type type;

    /**
     * Name of entity that has sent notification
     */
    private String emitter;
    /**
     * Text of notification
     */
    private String text;

    public Notification(Type type, String text, String emitter, long timestamp) {
        this.type = type;
        this.text = text;
        this.emitter = emitter;
        this.timestamp = timestamp;
    }

    public Notification(Type type, String text, String nick) {
        this(type, text, nick, System.currentTimeMillis());
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getDate() {
        return new Date(timestamp);
    }

    public boolean isCommand() {
        return false;
    }

    @Override
    public String toString() {
        String content = emitter + ": " + text;

        return "[" + getDate() + "] " + content;
    }
}
