package com.zcl.nettyRpc.manage;


import com.zcl.nettyRpc.client4serviceProvider.Rpc2ProviderHandler;
import com.zcl.nettyRpc.client4serviceProvider.SameInterfaceRpcHandlers;
import com.zcl.nettyRpc.codec.RpcDecoder;
import com.zcl.nettyRpc.codec.RpcEncoder;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 管理服务的所有连接器
 * Created by zhangchenlin on 17/5/12.
 */
public class ConnectManager {

    private static final Logger logger= LoggerFactory.getLogger(ConnectManager.class);

    private static final int connectTimeout=5000; //连接超时时间
    private static final int reconnectTime=10000;

    //用于保存连接的服务地址的信息
    private Map<InetSocketAddress,Rpc2ProviderHandler> connectHandlerMap=new ConcurrentHashMap<>();
    //用于保存服务器与服务提供者的连接handler
    private CopyOnWriteArrayList<Rpc2ProviderHandler> connectHandlerList=new CopyOnWriteArrayList<>();
    //用于存储服务接口对应一个或多个连接的map
    private Map<String,SameInterfaceRpcHandlers> interfaceandHandlersMap=new ConcurrentHashMap<>();

    private volatile static ConnectManager connectManager;


    private CountDownLatch countDownLatch;

    private ConnectManager(){};


    /**
     * 获取实例的单例设计模式
     * @return
     */
    public static ConnectManager getInstance() {
        if (connectManager == null) {
            synchronized (ConnectManager.class) {
                if (connectManager == null)
                    connectManager = new ConnectManager();
            }
        }
        return connectManager;
    }


    /**
     * 更新服务连接操作
     * @param newServerAddress
     * @param Service2IpMap
     */
    public void updateconnectServer(Set<String> newServerAddress, Map<String,Set<InetSocketAddress>> Service2IpMap)
    {

        if(newServerAddress!=null)
        {
            Set<InetSocketAddress> newServerNodeSet=new HashSet<>();
            //整理出需要连接的服务地址
            for(String address:newServerAddress)
            {
                String[] array=address.split(":");
                if(array.length==2)
                {
                    String host=array[0];
                    int port=Integer.parseInt(array[1]);
                    InetSocketAddress remote=new InetSocketAddress(host,port);
                    newServerNodeSet.add(remote);
                }
            }

            //清理没用的连接
            for(int i=0;i<connectHandlerList.size();i++)
            {
                Rpc2ProviderHandler handler=connectHandlerList.get(i);
                InetSocketAddress address=handler.getSocketAddress();
                if(!newServerNodeSet.contains(address))
                {
                    logger.info("remove and close invalid server node:{}",address);
                    handler.close();
                    connectHandlerList.remove(handler);
                    connectHandlerMap.remove(address);
                }
            }

            //如果有没有创建连接的服务，则去创建连接

            int needToConnectNum=0;

            for(InetSocketAddress serverNode:newServerNodeSet)
            {
                Rpc2ProviderHandler handler=connectHandlerMap.get(serverNode);
                if(handler==null)
                {
                    needToConnectNum++;
                }
            }

            if(needToConnectNum>0)
            {
                countDownLatch=new CountDownLatch(needToConnectNum);
                for(InetSocketAddress serverNode:newServerNodeSet)
                {
                    Rpc2ProviderHandler handler=connectHandlerMap.get(serverNode);
                    if(handler==null)
                    {
                        ConnectServerNode(serverNode);
                    }
                }
            }


            try {
                    if(countDownLatch!=null)
                    countDownLatch.await(connectTimeout,TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }


            //更新interfaceandHandlersMap
            for(String key:Service2IpMap.keySet())
            {
                SameInterfaceRpcHandlers sameInterfaceRpcHandlers=new SameInterfaceRpcHandlers();
                Set<InetSocketAddress> addresses=Service2IpMap.get(key);
                for(InetSocketAddress address:addresses)
                {
                    Rpc2ProviderHandler handler=connectHandlerMap.get(address);
                    if(handler!=null)
                    sameInterfaceRpcHandlers.addHandler(handler);
                }
                interfaceandHandlersMap.put(key,sameInterfaceRpcHandlers);
            }

            logger.info("current connectHandlerMap:{}",connectHandlerMap);
            logger.info("current connectHandlerList:{}",connectHandlerList);
            logger.info("current interfaceandHandlersMap",interfaceandHandlersMap);

        }else {
            logger.error("no available server node.all server nodes down!");
            for(Rpc2ProviderHandler handler:connectHandlerList)
            {
                logger.info("remove invalid servernode"+handler.getSocketAddress());
                handler.close();
            }
            connectHandlerList.clear();
            connectHandlerMap.clear();
            interfaceandHandlersMap.clear();
            logger.info("all maps have been cleared ");
        }





    }







    /**
     * 连接发布服务的节点到netty服务器
     * @param address
     */
    public void ConnectServerNode(final InetSocketAddress address)
    {
        logger.info("start connect to service-provider:{}",address);

        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,connectTimeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline=socketChannel.pipeline();
                        //TODO
                        pipeline.addLast(new RpcEncoder(Request.class));
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
                        pipeline.addLast(new RpcDecoder(Response.class));

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



                    }
                });

        bootstrap.connect(address).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess())
                    {
                        logger.info("success connect to service provider:{}",address);
                    }else
                    {
                        //不同的重连
                        logger.info("failed to connect to server:{},will reconnect {} millseconds later",address,reconnectTime);
                        channelFuture.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                ConnectServerNode(address);
                            }
                        },reconnectTime, TimeUnit.MILLISECONDS);
                    }
            }
        });



    }



    public  Rpc2ProviderHandler chooseHandler(String interfaceName)
    {
        SameInterfaceRpcHandlers handlers=interfaceandHandlersMap.get(interfaceName);
        if(handlers!=null)
        {
            return handlers.getSLBHandler();
        }else
            return null;
    }


}
