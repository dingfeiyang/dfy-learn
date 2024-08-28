package io.github.dingfeiyang.rpc.proxy;

import io.github.dingfeiyang.rpc.protocol.MyContent;
import io.github.dingfeiyang.rpc.transport.ClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

public class MyProxy {

    public static  <T>T proxyGet(Class<T> interfaceClass) {
        ClassLoader loader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = {interfaceClass};

        return (T) Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String className = interfaceClass.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();

                // 发送的具体数据
                MyContent content = new MyContent();
                content.setName(className);
                content.setMethodName(methodName);
                content.setArgs(args);
                content.setParameterTypes(parameterTypes);

                CompletableFuture<Object> resFuture = ClientFactory.transport(content);
                return resFuture.get();
            }
        });
    }
}
