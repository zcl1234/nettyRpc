package com.zcl.nettyRpc.registry;

import com.zcl.nettyRpc.manage.ConnectManager;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务发现
 * Created by zhangchenlin on 17/5/12.
 */
public class ServiceDiscovery {

    private static final Logger logger= LoggerFactory.getLogger(ServiceDiscovery.class);

    private  String CONNECT_ADDR;

    private final int SESSION_OUTTIME=5000;
    private CuratorFramework cf;

    //zookeeper中注册的服务节点
    private List<String> interfaceList=new LinkedList<>();
    private Set<InetSocketAddress> dataSet=new HashSet<>();

    private ConcurrentHashMap<String,Set<InetSocketAddress>> Service2IpMap=new ConcurrentHashMap<>();
    //客户端订阅的节点
    private List<String> interfaces;

    public ServiceDiscovery(String address,List<String> interfaces) throws InterruptedException {
        this.interfaces=interfaces;
        this.CONNECT_ADDR=address;
        connect();
        if(cf!=null) {
            watchRoot();
            watchNode("NettyRpc");

        }
        //Thread.sleep(Integer.MAX_VALUE);
    }

    private CuratorFramework connect()
    {
        //1.重试策略：初始时间为1，重试10次
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,10);

        //通过工厂建立连接
        cf= CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();
        cf.start();
        return cf;
    }

    /**
     * 监视根节点上服务的创建情况
     */
    private void watchRoot()
    {
        try {
            List<String> interfaceList=cf.getChildren().forPath("/NettyRpc");
            Set<String> data=new HashSet<>();
            for(String service:interfaceList)
            {
                if(interfaces.contains(service))
                {   //监视地址节点
                    watchNode("NettyRpc/"+service);
                    List<String> addressList=cf.getChildren().forPath("/NettyRpc/"+service);
                    for(String s:addressList)
                    {
                        data.add(s);
                        String[] array=s.split(":");
                        if(array.length==2)
                        {
                            String host=array[0];
                            int port=Integer.parseInt(array[1]);
                            dataSet.add(new InetSocketAddress(host,port));
                        }
                    }
                    Service2IpMap.put(service,dataSet);
                }
            }
            logger.info("nodeData:{}",data);
            logger.info("service2IpMap data:{}",Service2IpMap);
            logger.info("Service discovery triggered updating connecting server node");
            //更新连接操作
            ConnectManager.getInstance().updateconnectServer(data,Service2IpMap);

            System.out.println(data+"ip连接netty");


        } catch (Exception e) {
            logger.info("",e);
        }
    }






    private void watchNode(String Nodepath)
    {
        PathChildrenCache cache=new PathChildrenCache(cf,"/"+Nodepath,true);
        try {
            cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            cache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    switch(pathChildrenCacheEvent.getType())
                    {
                        case CHILD_ADDED:
                            logger.info("new service registered:{}",pathChildrenCacheEvent.getData().getPath());
                            watchRoot();
                            break;
                        case CHILD_REMOVED:
                            logger.info("registered service removes:{}",pathChildrenCacheEvent.getData().getPath());
                            watchRoot();
                            break;
                        case CHILD_UPDATED:
                            logger.info("registered service updated:{}",pathChildrenCacheEvent.getData().getPath());
                            watchRoot();
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
