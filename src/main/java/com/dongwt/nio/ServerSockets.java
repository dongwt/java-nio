package com.dongwt.nio;

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
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));
        channel.configureBlocking(false);//非阻塞模式

        ByteBuffer readBuffer = ByteBuffer.allocate(13);
        boolean flag = true;
        while (flag) {
            SocketChannel socketChannel = channel.accept();
            if (socketChannel == null) {
                System.out.println("waiting for accept ....");
                Thread.sleep(1000);
                continue;
            }
            int length = socketChannel.read(readBuffer);
            while (length != -1) {
                readBuffer.flip();
                while (readBuffer.hasRemaining()) {
                    System.out.print((char) readBuffer.get());
                }
                System.out.println("---------------------");
                readBuffer.clear();
                length = socketChannel.read(readBuffer);
            }
        }

        channel.close();

    }
}
