package com.messenger.client;

import java.io.*;

public class Console {

    BufferedReader in;
    BufferedWriter out;

    Console() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        OutputStreamWriter streamWriter = new OutputStreamWriter(System.out);

        in = new BufferedReader(streamReader);
        out = new BufferedWriter(streamWriter);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void print(String text) throws IOException {
        out.write(text);
        out.flush();
    }

    public void printLine(String text) throws IOException {
        out.write(text);
        out.newLine();
        out.flush();
    }

}
