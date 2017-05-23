package com.dongwt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-23 16:15
 **/
public class ServerSockets {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel channel =  ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        boolean flag = true;

        while (flag){
            SocketChannel socketChannel = channel.accept();
            int length = socketChannel.read(readBuffer);
            while(length != -1){
                readBuffer.flip();
                while (readBuffer.hasRemaining()){
                    System.out.print((char) readBuffer.get());
                }
                readBuffer.clear();
                length = socketChannel.read(readBuffer);
            }
        }

        channel.close();

    }
}
