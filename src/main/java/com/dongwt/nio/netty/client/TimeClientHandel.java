package com.dongwt.nio.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-26 14:49
 **/
public class TimeClientHandel extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i<100; i++){
            String message = i + "hello  \r\n";
            ByteBuf request = Unpooled.buffer(message.getBytes().length);
            request.writeBytes(message.getBytes());
            ctx.writeAndFlush(request);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("now is:" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
}
