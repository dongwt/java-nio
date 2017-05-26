package com.dongwt.nio.test.netty;

import com.dongwt.nio.netty.server.TimeServer;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-26 14:22
 **/
public class ServerTest {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new TimeServer().bind(port);
    }
}
