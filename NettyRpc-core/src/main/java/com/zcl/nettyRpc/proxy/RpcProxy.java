package com.zcl.nettyRpc.proxy;

import com.zcl.nettyRpc.client.RPCFuture;
import com.zcl.nettyRpc.client4serviceProvider.Rpc2ProviderHandler;
import com.zcl.nettyRpc.manage.ConnectManager;
import com.zcl.nettyRpc.protocol.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 同步代理类
 * Created by 626hp on 2017/5/13.
 */
public class RpcProxy<T> implements InvocationHandler{

        private static final Logger logger= LoggerFactory.getLogger(RpcProxy.class);

        private Class<T> clazz;

        public RpcProxy(Class<T> clazz)
        {
            this.clazz=clazz;
        }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("come reflect.....");
        Request request=new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        Rpc2ProviderHandler handler= ConnectManager.getInstance().chooseHandler(method.getDeclaringClass().getName());
        if(handler==null)
        {
            logger.error("no such service published",method.getDeclaringClass().getName());
            return null;
        }
        RPCFuture rpcFuture=handler.sendRequestSync(request);
        //同步阻塞方式
        return rpcFuture.get();

    }
}
