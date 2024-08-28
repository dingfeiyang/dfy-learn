package io.github.dingfeiyang.rpc.transport;

import io.github.dingfeiyang.rpc.protocol.MyContent;
import io.github.dingfeiyang.rpc.protocol.MyHeader;
import io.github.dingfeiyang.rpc.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {

    private static final ClientFactory factory;
    private static final ConcurrentHashMap<InetSocketAddress, ClientPool> outBox = new ConcurrentHashMap<>();
    private static final int poolSize = 2;
    private static final Random rand = new Random();

    static {
        factory = new ClientFactory();
    }
    private ClientFactory() {}
    public static ClientFactory getFactory() {
        return factory;
    }

    public static CompletableFuture<Object> transport(MyContent content) {
        byte[] msgBody = SerDerUtil.ser(content);
        MyHeader header = MyHeader.createHeader(msgBody);
        byte[] msgHeader = SerDerUtil.ser(header);

        NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
        long requestId = header.getRequestId();
        CompletableFuture<Object> res = new CompletableFuture<>();
        ResponseMappingCallback.addCallBack(requestId, res);
        byteBuf.writeBytes(msgHeader);
        byteBuf.writeBytes(msgBody);
        clientChannel.writeAndFlush(byteBuf);

        return res;
    }

    public NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = outBox.get(address);
        if (clientPool == null) {
            synchronized (this) {
                if (clientPool == null) {
                    outBox.putIfAbsent(address, new ClientPool(poolSize));
                    clientPool = outBox.get(address);
                }
            }
        }

        int index = rand.nextInt(poolSize);
        if (clientPool.clients[index] != null && clientPool.clients[index].isActive()) {
            return clientPool.clients[index];
        } else {
            synchronized (clientPool.lock[index]) {
                if (clientPool.clients[index] == null || !clientPool.clients[index].isActive()) {
                    clientPool.clients[index] = create(address);
                }
            }
        }

        return clientPool.clients[index];
    }

    private NioSocketChannel create(InetSocketAddress address) {
        NioEventLoopGroup clientWorker = new NioEventLoopGroup(1);

        Bootstrap bs = new Bootstrap();
        ChannelFuture connectFuture = bs.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ClientResponses());
                    }
                }).connect(address);
        try {
            return (NioSocketChannel)connectFuture.sync().channel();
        } catch (InterruptedException e) {
            System.out.println("exception： " + e);
        }
        return null;
    }
}
