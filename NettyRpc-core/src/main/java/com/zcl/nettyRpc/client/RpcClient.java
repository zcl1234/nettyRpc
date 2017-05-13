package com.zcl.nettyRpc.client;

import com.zcl.nettyRpc.proxy.RpcProxy;
import com.zcl.nettyRpc.registry.ServiceDiscovery;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by 626hp on 2017/5/13.
 */
public class RpcClient {

    private static final String zookeeperAddress="172.16.34.136:2181,172.16.34.136:2183,172.16.34.136:2184";

    private ServiceDiscovery serviceDiscovery;
    public RpcClient(String zookeeperAddress, List<String> interfaces) throws InterruptedException {
         this.serviceDiscovery=new ServiceDiscovery(zookeeperAddress,interfaces);
    }

    /**
     *创建动态代理类
     * @param <T>
     * @return
     */
    public static <T> T createProxy(Class<T> interfaceClass)
    {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                                         new Class<?>[]{interfaceClass},
                                          new RpcProxy<T>(interfaceClass));
    }
}
