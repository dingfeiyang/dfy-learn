package io.github.dingfeiyang.rpc.transport;

import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher {
    private static Dispatcher dis = null;

    private static ConcurrentHashMap<String,Object> invokeMap = new ConcurrentHashMap<>();
    private Dispatcher() {

    }
    static {
        dis = new Dispatcher();
    }
    public static Dispatcher getDis() {
        return dis;
    }

    public void register(String key, Object obj){
        invokeMap.put(key, obj);
    }

    public Object get(String key) {
        return invokeMap.get(key);
    }
}
