package com.messenger.common;

public enum SystemCode {

    CLOSE,
    PING,
    PONG;

    @Override
    public String toString() {
        return super.toString();
    }

    public static SystemCode fromValue(String val) {
        return Enum.valueOf(SystemCode.class, val);
    }

}
