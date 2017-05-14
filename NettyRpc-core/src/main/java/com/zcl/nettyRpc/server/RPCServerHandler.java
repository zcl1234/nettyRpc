package com.zcl.nettyRpc.server;

import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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

    public RPCServerHandler(Map<String,Object> serverBeanMap)
    {
        this.serverBeanMap=serverBeanMap;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("====rpc server active "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("====rpc server Inactive "+ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        if(request!=null)
            logger.info("===rpc read message"+request);
    }

    /*
        @Override
        public void channelRead0(final ChannelHandlerContext channelHandlerContext,final Request request) throws Exception {
            logger.info("====rpc server channelRead0:"+channelHandlerContext.channel().remoteAddress());

            RPCServer.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("receive request:"+request.getRequestId()
                            +"className"+request.getClassName()
                            +"methodName:"+request.getMethodName());
            Response response=new Response();
            response.setRequestId(request.getRequestId());
            try {
                String className = request.getClassName();
                Object serverBean = serverBeanMap.get(className);

                String methodName = request.getMethodName();
                Class<?>[] paramterTypes = request.getParameterTypes();
                Object[] parameters = request.getParameters();

                //Java reflect
                Method method = serverBean.getClass().getMethod(methodName, paramterTypes);
                //TODO UNDERSTAND
                method.setAccessible(true);
                Object result = method.invoke(serverBean, parameters);

                response.setResult(result);
            }catch (Exception e)
            {
                logger.error("Exception:{}",e);
                response.setError(e.getMessage());
            }
            channelHandlerContext.writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("send response for requestId:{}",request.getRequestId());
                }
            });
                }
            });
        }
        */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.info("rpc server caught exception: "+ctx.channel().remoteAddress()+"|"+cause.getMessage());
    }
}
