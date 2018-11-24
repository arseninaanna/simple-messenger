package com.messenger.client;

import java.io.*;

class Console {

    private BufferedReader in;
    private BufferedWriter out;

    Console() {
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
