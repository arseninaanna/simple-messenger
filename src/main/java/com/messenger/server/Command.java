package com.messenger.server;

import com.messenger.common.Packet;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

public class Command {

    private static final String STR_QUOTE = "`";

    SocketClient socketClient;
    private Packet packet;

    private String command; // Command name in lower case
    private String[] params; // Parsed command parameters

    Command(Packet packet, SocketClient client) {
        if (!packet.isCommand()) {
            throw new InvalidParameterException("Supplied packet is not a command");
        }

        this.socketClient = client;
        this.packet = packet;

        parsePacketText();
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }

    private void parsePacketText() {
        // Raw parse of command text
        String[] parts = packet.getText().substring(1).trim().split("\\w");

        // Flush old parsed data and set default fallback
        command = "";
        params = new String[0];

        if (parts.length >= 1) {
            // Read the command
            command = parts[0].toLowerCase();
        }
        if (parts.length >= 2) {
            // Prepare params
            params = prepareParams(parts).toArray(params);
        }
    }

    private List<String> prepareParams(String[] parts) {
        LinkedList<String> tmpParams = new LinkedList<>();

        boolean quoting = false;
        for (int i = 1; i < parts.length; i++) {
            String cParam = parts[i].trim();

            if (!quoting && cParam.startsWith(STR_QUOTE)) {
                cParam = cParam.substring(1); // Remove quote
                quoting = true;
            }
            boolean shouldConcat = quoting && !tmpParams.isEmpty();
            if (quoting && cParam.endsWith(STR_QUOTE)) {
                cParam = cParam.substring(0, cParam.length() - 1); // Remove quote
                quoting = false;
            }

            if (shouldConcat) {
                // Concatenate this part with last element
                cParam = tmpParams.pollLast() + " " + cParam;
            }

            tmpParams.add(cParam);
        }

        return tmpParams;
    }

}
