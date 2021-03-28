package com.dongwt.nio.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BioTest {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(8080));
        System.out.println("ServerSocket start success");
        while (true){
            final Socket client = server.accept();//阻塞
            System.out.println("client:"+ client.getInetAddress().getHostAddress());
//            System.out.println("please input:");
//            System.in.read();
            new Thread(new Runnable() {
                public void run() {
                    InputStream in = null;
                    try {
                        in = client.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        while (true){
                            String request = reader.readLine();//阻塞
                            System.out.println("request:"+ request);
                            OutputStream out = client.getOutputStream();
                            out.write((request+"\n\n").getBytes());
                            out.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        }
    }
}
