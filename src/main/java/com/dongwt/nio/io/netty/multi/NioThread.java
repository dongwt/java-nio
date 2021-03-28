package com.dongwt.nio.io.netty.multi;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public  class NioThread extends Thread {
    public static int selectorNum = 0;
    public static BlockingQueue<SocketChannel>[] queues;
    public static AtomicInteger idx = new AtomicInteger();

    private Selector selector;
    private int id = 0;
    private boolean boss = false;

    //boss
    public NioThread(Selector selector, int selectorNum) {
        this.selector = selector;
        this.selectorNum = selectorNum;
        this.boss = true;

        queues = new LinkedBlockingQueue[selectorNum];
        for (int i = 0; i < selectorNum; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }
        System.out.println("boss 启动");
    }

    //worker
    public NioThread(Selector selector) {
        this.selector = selector;
        id = idx.getAndIncrement() % selectorNum;
        System.out.println("worker:" + id + " 启动");
    }

    @Override
    public void run() {
        try {
            while (true) {

                while (selector.select(10) > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }


                if (!boss && !queues[id].isEmpty()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    SocketChannel client = queues[id].take();
                    client.register(selector, SelectionKey.OP_READ, buffer);
                    System.out.println("新客戶端：" + client.socket().getPort() + " 分配至worker：" + id);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey key) throws Exception {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel client = ssc.accept();
        client.configureBlocking(false);

        int num = idx.getAndIncrement() % selectorNum;
        queues[num].add(client);
    }

    private void readHandler(SelectionKey key) throws Exception {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        int read = 0;
        while (true) {
            read = client.read(buffer);
            if (read > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    client.write(buffer);
                }
                buffer.clear();
            } else if (read == 0) {
                break;
            } else {
                client.close();
                break;
            }
        }
    }
}
