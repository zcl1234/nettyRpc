package com.zcl.nettyRpc.server;

import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务处理Handler
 * Created by 626hp on 2017/5/13.
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<Request>{

    private static final Logger logger= LoggerFactory.getLogger(RPCServerHandler.class);
    private Map<String,Object> serverBeanMap=new ConcurrentHashMap<>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        logger.info("====rpc server channelRead0:"+channelHandlerContext.channel().remoteAddress());


        logger.info("receive request:"+request.getRequestId()
                        +"className"+request.getClassName()
                        +"methodName:"+request.getMethodName());
        Response response=new Response();
        response.setRequestId(request.getRequestId());
        String className=request.getClassName();
        Object serverBean=serverBeanMap.get(className);

        String methodName=request.getMethodName();
        Class<?>[] paramterTypes=request.getParameterTypes();
        Object[] parameters=request.getParameters();

        //Java reflect
        Method method=




    }
}
