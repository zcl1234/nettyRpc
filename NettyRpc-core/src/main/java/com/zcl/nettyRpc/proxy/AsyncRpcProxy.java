package com.zcl.nettyRpc.proxy;

import com.zcl.nettyRpc.client.AsyncRPCCallback;
import com.zcl.nettyRpc.client.RPCFuture;
import com.zcl.nettyRpc.client4serviceProvider.Rpc2ProviderHandler;
import com.zcl.nettyRpc.manage.ConnectManager;
import com.zcl.nettyRpc.protocol.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 异步代理类
 * Created by zhangchenlin on 17/5/15.
 */
public class AsyncRpcProxy {

    private static final Logger logger= LoggerFactory.getLogger(AsyncRpcProxy.class);

    private Class<?> clazz;
  //  private CountDownLatch countDownLatch;
    public AsyncRpcProxy(Class<?> clazz)
    {
        this.clazz=clazz;
    }

    /*
    public AsyncRpcProxy(Class<?> clazz, CountDownLatch countDownLatch)
    {
        this.clazz=clazz;
        this.countDownLatch=countDownLatch;
    }
    */
    /**
     * 异步调用方法
     * @param methodName
     * @param callback
     * @param args
     * @return
     */
    public RPCFuture call(String methodName, AsyncRPCCallback callback,Object... args)
    {

        Request request=new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(clazz.getName());
        request.setMethodName(methodName);
        request.setParameters(args);
        Class[] paramTypes=new Class[args.length];
       for(int i=0;i<args.length;i++)
       {
            paramTypes[i]=getClassType(args[i]);
       }
        request.setParameterTypes(paramTypes);
        Rpc2ProviderHandler handler= ConnectManager.getInstance().chooseHandler(clazz.getName());
        RPCFuture rpcFuture=handler.sendRequestAsync(request,callback);
        return rpcFuture;
    }

    public Class<?> getClassType(Object object)
    {
        Class<?> classType=object.getClass();
        String className=classType.getName();
        if ("java.lang.Integer".equals(className)) {
            return Integer.TYPE;
        } else if ("java.lang.Long".equals(className)) {
            return Long.TYPE;
        } else if ("java.lang.Float".equals(className)) {
            return Float.TYPE;
        } else if ("java.lang.Double".equals(className)) {
            return Double.TYPE;
        } else if ("java.lang.Character".equals(className)) {
            return Character.TYPE;
        } else if ("java.lang.Boolean".equals(className)) {
            return Boolean.TYPE;
        } else if ("java.lang.Short".equals(className)) {
            return Short.TYPE;
        } else if ("java.lang.Byte".equals(className)) {
            return Byte.TYPE;
        }
        return classType;
    }


}
