package io.github.dingfeiyang.rpc;

import io.github.dingfeiyang.rpc.proxy.MyProxy;
import io.github.dingfeiyang.rpc.service.Car;
import io.github.dingfeiyang.rpc.service.CarImpl;
import io.github.dingfeiyang.rpc.service.CarInfo;
import io.github.dingfeiyang.rpc.transport.Dispatcher;
import io.github.dingfeiyang.rpc.transport.ServerDecode;
import io.github.dingfeiyang.rpc.transport.ServerRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * 手写RPC 功能
 *
 *     1，先假设一个需求，写一个RPC
 *     2，来回通信，连接数量，拆包？
 *     3，动态代理呀，序列化，协议封装
 *     4，连接池
 *     5，就像调用本地方法一样去调用远程的方法，面向java中就是所谓的 面向interface开发
 */
public class MyRpcTest {

    @Test
    public void startServer() {
        Dispatcher dis = Dispatcher.getDis();
        CarImpl car = new CarImpl();
        dis.register(Car.class.getName(), car);

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup(3);
        ServerBootstrap sb = new ServerBootstrap();
        ChannelFuture channelFuture = sb.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("server accept client port: " + ch.remoteAddress().getPort());

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ServerRequestHandler(dis));
                    }
                }).bind(new InetSocketAddress("localhost", 9090));
        System.out.println("server startup");
        try {
            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Test
    public void startClient() {
        Car car = MyProxy.proxyGet(Car.class);
//        car.drive("dingfeiyang");
        CarInfo carInfo = car.get("11");
        System.out.println(carInfo);
    }

}
