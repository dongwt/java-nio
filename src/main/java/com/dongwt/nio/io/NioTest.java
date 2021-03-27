package com.dongwt.nio.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class NioTest {
    public static void main(String[] args) throws Exception{
        List<SocketChannel> clients = new LinkedList<>();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8081));
        server.configureBlocking(false);//非阻塞
        System.out.println("ServerSocketChannel start success");

        while (true){
            SocketChannel client = server.accept();
            if(null == client){

            }else {
                client.configureBlocking(false);//非阻塞
                System.out.println("client address:" + client.getRemoteAddress());
                clients.add(client);
            }


            for(SocketChannel item : clients){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int count = item.read(buffer);
                if(count > 0){
                    buffer.flip();
                    byte[] bytes = new byte[count];
                    buffer.get(bytes);
                    String request = new String(bytes);
                    System.out.println("request:" + request);
                    buffer.clear();

                    ByteBuffer outBuffer = ByteBuffer.allocate(1024);
                    outBuffer.put((request+"\n\n").getBytes());
                    outBuffer.flip();
                    item.write(outBuffer);
                }

            }
        }
    }
}
