package io.github.dingfeiyang.rpc.transport;

import io.github.dingfeiyang.rpc.protocol.MyPack;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseMappingCallback {
    private static ConcurrentHashMap<Long, CompletableFuture<Object>> mapping = new ConcurrentHashMap<>();

    public static void addCallBack(long requestId, CompletableFuture<Object> cb) {
        mapping.putIfAbsent(requestId, cb);
    }

    public static void runCallBack(MyPack msg) {
        CompletableFuture<Object> future = mapping.get(msg.getHeader().getRequestId());
        future.complete(msg.getContent().getRes());
        removeCallBack(msg.getHeader().getRequestId());
    }

    private static void removeCallBack(long requestId) {
        mapping.remove(requestId);
    }
}
