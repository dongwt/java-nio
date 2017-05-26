package com.dongwt.nio.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-26 14:41
 **/
public class TimeClient {

    public void connect(int port , String host){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ClientChildChannelHandler());

            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            group.shutdownGracefully();
        }
    }
}
