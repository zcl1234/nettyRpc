package com.zcl.nettyRpc.server;

import com.zcl.nettyRpc.codec.RpcDecoder;
import com.zcl.nettyRpc.codec.RpcEncoder;
import com.zcl.nettyRpc.codecUntil.*;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import com.zcl.nettyRpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RPC服务器
 * Created by zhangchenlin on 17/5/13.
 */
public class RPCServer {

    private static final Logger logger= LoggerFactory.getLogger(RPCServer.class);

    private ServiceRegistry serviceRegistry;
    private String serverAddress;
    //存放服务与服务对象之间的映射关系
    private static Map<String,Object> serviceBeanMap=new ConcurrentHashMap<>();

    //处理任务采用线程池，提高性能
    private static ExecutorService executorService;
    public RPCServer(String serverAddress,List<Class<?>> services)
    {
        this.serverAddress=serverAddress;
        serviceRegistry=new ServiceRegistry();
        publishService(services);
       // startServer();
    }

    /**
     * 开启服务器
     * @param
     */
    //该注解表示该方法在类实例化后实现
   // @PostConstruct
    public void startServer()
    {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline=socketChannel.pipeline();
                            channelPipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
                           // channelPipeline.addLast(new RpcDecoder(Request.class));
                            //channelPipeline.addLast(new RpcEncoder(Response.class));
                            //channelPipeline.addLast(new KryoDecoder(new KryoCodecUntil(kryoPoolFactory.getKryoPoolInstance())));
                            //channelPipeline.addLast(new KryoEncoder(new KryoCodecUntil(kryoPoolFactory.getKryoPoolInstance())));
                            channelPipeline.addLast(new ProtostuffDecoder(new ProtostuffCodecUntil(Request.class)));
                            channelPipeline.addLast(new ProtostuffEncoder(new ProtostuffCodecUntil(Response.class)));
                            channelPipeline.addLast(new RPCServerHandler(serviceBeanMap));
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                      .option(ChannelOption.TCP_NODELAY,true)
                        .childOption(ChannelOption.SO_KEEPALIVE,true);

            String[] array=serverAddress.split(":");
            String host=array[0];
            int port=Integer.parseInt(array[1]);

            ChannelFuture future=bootstrap.bind(host,port).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess())
                    {
                        logger.info("server start sucess:{}",serverAddress);
                    }
                }
            });
            //等待绑定端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("server started error:{}",serverAddress);
        } finally {
           workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     *发布服务
     * @param services
     */
    public void publishService(List<Class<?>> services) {
        try {
            for (Class<?> service : services) {
                String className=service.getName();
                Object object = service.newInstance();  //保存要发布类的实例化对象
                for(Class<?> clazz:service.getInterfaces()) {
                    serviceRegistry.createInterfaceAddressNode(clazz.getName(), serverAddress);  //注册接口服务
                    serviceBeanMap.put(clazz.getName(),object);  //保存接口名字和实例化对象的映射
                }
            }
        }catch (Exception e)
        {
            logger.error("publish service failed:{}",e);
        }
    }

    /**
     * 提交任务
     * @param task
     */
    public static void submit(Runnable task)
    {
        if(executorService==null)
        {
            synchronized (RPCServer.class)
            {
                if(executorService==null)
                    executorService= Executors.newFixedThreadPool(16);
            }
        }
        executorService.submit(task);
    }


}
