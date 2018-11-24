package com.messenger.common;

public enum SystemCode {

    CLOSE;

    public static SystemCode fromValue(String val) {
        return Enum.valueOf(SystemCode.class, val);
    }

}
