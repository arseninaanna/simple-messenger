package com.messenger.client;

import com.messenger.common.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class StartClient {

    static String serverAddr = null;
    static int serverPort = 8090;
    static int timeout = 15 * 1000;

    public static void main(String[] args) {
        try {
            //Socket soc = new Socket(serverAddr, serverPort);
            //soc.setSoTimeout(timeout);

            //BufferedInputStream socIn = new BufferedInputStream(soc.getInputStream());
            //BufferedOutputStream socOut = new BufferedOutputStream(soc.getOutputStream());

            BufferedInputStream sysIn = new BufferedInputStream(System.in);
            BufferedOutputStream sysOut = new BufferedOutputStream(System.out);

            sysOut.write("asd".getBytes());
            sysOut.write(Message.msg());
            sysOut.flush();


        } catch (ConnectException e) {
            System.out.println("Failed to connect to server. Is it running?");
        } catch (UnknownHostException e) {
            System.out.println("Failed to find host. Check ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
