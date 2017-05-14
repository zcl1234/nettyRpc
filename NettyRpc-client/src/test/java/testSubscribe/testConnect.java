package testSubscribe;

import com.zcl.nettyRpc.client4serviceProvider.Rpc2ProviderHandler;
import com.zcl.nettyRpc.codec.RpcDecoder;
import com.zcl.nettyRpc.codec.RpcEncoder;
import com.zcl.nettyRpc.manage.ConnectManager;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by 626hp on 2017/5/14.
 */
public class testConnect {

    @Test
    public void test()
    {
      final InetSocketAddress address=new InetSocketAddress("localhost",2345);
        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline=socketChannel.pipeline();
                        //TODO
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4,0,0));
                        pipeline.addLast(new RpcDecoder(Request.class));
                        pipeline.addLast(new RpcEncoder(Response.class));
                      /*
                        pipeline.addLast(new Rpc2ProviderHandler() {
                            @Override
                            public void handlerCallback(Rpc2ProviderHandler handler, boolean isActive) {
                                if(isActive)
                                {
                                    logger.info("Active:"+handler.getSocketAddress());
                                    connectHandlerList.add(handler);
                                    connectHandlerMap.put(handler.getSocketAddress(),handler);
                                    countDownLatch.countDown();
                                }else
                                {
                                    logger.info("InActive:"+handler.getSocketAddress());
                                }
                            }
                        });
                        */



                    }
                });

        bootstrap.connect("localhost",2345).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess())
                {
                    System.out.println("success connect to service provider:{}");
                }else
                {
                    /*
                    //不同的重连
                    logger.info("failed to connect to server:{},will reconnect {} millseconds later",address,reconnectTime);
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ConnectServerNode(address);
                        }
                    },reconnectTime, TimeUnit.MILLISECONDS);
                    */
                }
            }
        });

    }
}
