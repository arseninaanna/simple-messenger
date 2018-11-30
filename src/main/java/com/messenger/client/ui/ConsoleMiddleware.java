package com.messenger.client.ui;

import java.io.*;

/**
 * Wrapper to work with console
 * @deprecated
 */
class ConsoleMiddleware {

    private BufferedReader in;
    private BufferedWriter out;

    ConsoleMiddleware() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        OutputStreamWriter streamWriter = new OutputStreamWriter(System.out);

        in = new BufferedReader(streamReader);
        out = new BufferedWriter(streamWriter);
    }

    String readLine() throws IOException {
        return in.readLine();
    }

    void print(String text) throws IOException {
        out.write(text);
        out.flush();
    }

    void printLine(String text) throws IOException {
        out.write(text);
        out.newLine();
        out.flush();
    }

}
