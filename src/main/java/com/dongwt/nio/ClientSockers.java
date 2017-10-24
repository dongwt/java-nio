package com.dongwt.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-23 16:28
 **/
public class ClientSockers {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);//非阻塞模式
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));

        while (!socketChannel.finishConnect()){
            System.out.println("waiting for connect ....");
            Thread.sleep(1000);
        }

        for (int i =0; i<50; i++){
        String data = "hello world !";

            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.clear();
            writeBuffer.put(data.getBytes());
            writeBuffer.flip();

            while (writeBuffer.hasRemaining()){
                socketChannel.write(writeBuffer);
            }
        }

        socketChannel.close();

    }

}
