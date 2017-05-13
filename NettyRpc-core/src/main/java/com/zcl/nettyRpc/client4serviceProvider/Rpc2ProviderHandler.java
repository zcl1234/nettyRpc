package com.zcl.nettyRpc.client4serviceProvider;

import com.zcl.nettyRpc.client.RPCFuture;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器连接远程服务提供者的Handler
 * Created by zhangchenlin on 17/5/12.
 */
public abstract class Rpc2ProviderHandler extends SimpleChannelInboundHandler<Response>{
    private static final Logger logger= LoggerFactory.getLogger(Rpc2ProviderHandler.class);

    private ConcurrentHashMap<String,RPCFuture> requestId2Future=new ConcurrentHashMap<>();
    public static InetSocketAddress socketAddress;
    public Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel=ctx.channel();
        socketAddress=(InetSocketAddress)ctx.channel().remoteAddress();
        handlerCallback(channel.pipeline().get(Rpc2ProviderHandler.class),true);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        handlerCallback(channel.pipeline().get(Rpc2ProviderHandler.class),false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        String requestId = response.getRequestId();
        RPCFuture rpcFuture = requestId2Future.get(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(response);
            requestId2Future.remove(requestId);
        }
    }

    public RPCFuture  sendRequestSync(Request request)
    {
        RPCFuture rpcFuture=new RPCFuture(request);
        requestId2Future.put(request.getRequestId(),rpcFuture);
        channel.writeAndFlush(request);
        return rpcFuture;
    }


    public abstract void  handlerCallback(Rpc2ProviderHandler handler,boolean isActive);

    public  InetSocketAddress getSocketAddress()
    {
        return socketAddress;
    }

    public void close()
    {
        channel.close();
    }

}
