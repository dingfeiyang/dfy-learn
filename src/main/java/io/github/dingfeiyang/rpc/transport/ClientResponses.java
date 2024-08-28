package io.github.dingfeiyang.rpc.transport;

import io.github.dingfeiyang.rpc.protocol.MyPack;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientResponses extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyPack responsePack = (MyPack) msg;
        System.out.println("client rec: " + responsePack.getContent().getRes());
        ResponseMappingCallback.runCallBack(responsePack);
    }
}
