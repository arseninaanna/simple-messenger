package com.messenger.common;

/**
 * Possible system commands
 */
public enum SystemCode {

    CLOSE, // End current session
    PING, // Ping message from user
    PONG, // Pong message from server
    ;

    public String code() {
        return super.name();
    }

    public static SystemCode fromValue(String value) {
        return Enum.valueOf(SystemCode.class, value);
    }

}
