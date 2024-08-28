package io.github.dingfeiyang.rpc.transport;

import io.github.dingfeiyang.rpc.protocol.MyContent;
import io.github.dingfeiyang.rpc.protocol.MyHeader;
import io.github.dingfeiyang.rpc.protocol.MyPack;
import io.github.dingfeiyang.rpc.util.SerDerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    private Dispatcher dispatcher;
    public ServerRequestHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyPack myPack = (MyPack) msg;
        MyHeader header = myPack.getHeader();
        MyContent content = myPack.getContent();

        ctx.executor().execute(() -> {
            Object o = dispatcher.get(content.getName());
            Class<?> clazz = o.getClass();
            Object res = null;
            try {
                Method method = clazz.getMethod(content.getMethodName(), content.getParameterTypes());
                res = method.invoke(o, content.getArgs());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            MyContent resContent = new MyContent();
            resContent.setRes(res);
            byte[] resContentBytes = SerDerUtil.ser(resContent);

            MyHeader resHeader = new MyHeader();
            resHeader.setRequestId(header.getRequestId());
            resHeader.setFlag(0x14141424);
            resHeader.setDataLen(resContentBytes.length);
            byte[] resHeaderBytes = SerDerUtil.ser(resHeader);

            ByteBuf resByteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(
                    resHeaderBytes.length + resContentBytes.length);
            resByteBuf.writeBytes(resHeaderBytes);
            resByteBuf.writeBytes(resContentBytes);
            ctx.writeAndFlush(resByteBuf);
        });
    }
}
