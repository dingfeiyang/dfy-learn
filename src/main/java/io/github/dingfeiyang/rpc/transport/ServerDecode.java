package io.github.dingfeiyang.rpc.transport;

import io.github.dingfeiyang.rpc.protocol.MyContent;
import io.github.dingfeiyang.rpc.protocol.MyHeader;
import io.github.dingfeiyang.rpc.protocol.MyPack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class ServerDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= 113) {
            byte[] bytes = new byte[113];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            MyHeader header = (MyHeader)oin.readObject();
            //通信的协议
            if (byteBuf.readableBytes() - 113 >= header.getDataLen()) {
                // 移动指针
                byteBuf.readBytes(113);
                byte[] data = new byte[(int)header.getDataLen()];
                byteBuf.readBytes(data);
                ByteArrayInputStream bin = new ByteArrayInputStream(data);
                ObjectInputStream obin = new ObjectInputStream(bin);
                MyContent content = (MyContent)obin.readObject();
                list.add(new MyPack(header, content));
            }
        }
    }
}
