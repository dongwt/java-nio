package com.dongwt.nio.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleNettyTest {
    public static void main(String[] args) throws Exception {
        SimpleNetty simpleNetty = new SimpleNetty();
        simpleNetty.start();
    }

    public static class SimpleNetty{
        private ServerSocketChannel server = null;
        private Selector selector = null;
        int port = 9090;

        public void initServer() throws Exception{
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selector = Selector.open();

            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("ServerSocketChannel start success");
        }

        public void start() throws Exception {
            initServer();

            while (true){
                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size() + " size");

                if (selector.select() > 0){//阻塞，批量调一次内核  select poll epoll
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if(key.isAcceptable()){
                            acceptHandler(key);
                        }else if(key.isReadable()){
                            readHandler(key);
                        }
                    }
                }
            }
        }

        private void acceptHandler(SelectionKey key) throws Exception{
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.register(selector,SelectionKey.OP_READ,buffer);
            System.out.println("new client:" + client.getRemoteAddress());
        }

        private void readHandler(SelectionKey key) throws Exception{
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();

            int read = 0;
            while (true) {
                read = client.read(buffer);
                if(read > 0){
                    buffer.flip();
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }else if(read == 0){
                    break;
                }else {
                    client.close();
                    break;
                }
            }
        }
    }






}
