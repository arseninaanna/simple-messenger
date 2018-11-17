package com.messenger.common;

/**
 * Class for client-server messages conversion
 */
public class NotificationSerializer {

    /**
     * TODO
     * @param data
     * @return
     */
    public byte[] serizalize(Notification data) {
        return new byte[0];
    }

    /**
     * TODO
     * @param data
     * @return
     */
    public Notification deserialize(byte[] data) {
        return new Notification(Notification.Type.MESSAGE, "", "");
    }

}
