package io.github.dingfeiyang.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientPool {

    public NioSocketChannel[] clients;
    public Object[] lock;
    public ClientPool(int poolSize) {
        clients = new NioSocketChannel[poolSize];
        lock = new Object[poolSize];
        for (int i = 0; i <poolSize; i++) {
            lock[i] = new Object();
        }
    }
}
