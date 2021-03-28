package com.dongwt.nio.io.netty.multi;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public  class MultiNetty {
    private ServerSocketChannel server = null;
    private Selector selector1 = null;
    private Selector selector2 = null;
    private Selector selector3 = null;
    int port = 9090;

    public void initServer() throws Exception {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));

        selector1 = Selector.open();
        selector2 = Selector.open();
        selector3 = Selector.open();

        server.register(selector1, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception {
        initServer();

        NioThread t1 = new NioThread(selector1, 2);
        NioThread t2 = new NioThread(selector2);
        NioThread t3 = new NioThread(selector3);

        t1.start();

        Thread.sleep(1000);

        t2.start();
        t3.start();

        System.out.println("服务启动完毕");
    }
}
