package com.zcl.nettyRpc.client4serviceProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *用于负载均衡，即，相同的服务的由不同的服务器发布，则规定访问的方式
 * Created by zhangchenlin on 17/5/12.
 */
public class SameInterfaceRpcHandlers {
    private List<Rpc2ProviderHandler> handlers;
    private AtomicInteger number=new AtomicInteger(0);

    public SameInterfaceRpcHandlers()
    {
        this.handlers=new ArrayList<>();
    }

    public Rpc2ProviderHandler getSLBHandler()
    {
        if(handlers==null||handlers.size()<1)
        {
            return null;
        }
        int num=number.getAndIncrement()%handlers.size();
        return  handlers.get(num);
    }

    public void addHandler(Rpc2ProviderHandler handler)
    {
        handlers.add(handler);
    }


    public String toString()
    {
        String value="";
        for(Rpc2ProviderHandler handler:handlers)
        {
            value+=handler.getSocketAddress();
        }
        return "SameInterfaceRpcHandlers["+value+"]";
    }

}
