package com.dongwt.nio.test.netty;

import com.dongwt.nio.netty.client.TimeClient;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-26 14:22
 **/
public class ClientTest {

    public static void main(String[] args){
        int port = 8080;
        String host = "127.0.0.1";

        new TimeClient().connect(port,host);
    }
}
