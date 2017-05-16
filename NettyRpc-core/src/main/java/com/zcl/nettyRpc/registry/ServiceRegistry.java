package com.zcl.nettyRpc.registry;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rpc注册服务
 * Created by zhangchenlin on 17/5/12.
 */
public class ServiceRegistry {

    private static final Logger logger= LoggerFactory.getLogger(ServiceRegistry.class);

   private final String CONNECT_ADDR="172.16.34.141:2181,172.16.34.141:2183,172.16.34.141:2184";
   // private final String CONNECT_ADDR="192.168.137.134:2181,192.168.137.134:2182,192.168.137:2183";
    private final int SESSION_OUTTIME=5000;

    private CuratorFramework cf=null;

    public ServiceRegistry()
    {
        cf=connect();
        if(cf!=null)
        {
            setRootNode();
        }

    }

    /**
     * 创建根节点
     */
    private void setRootNode()
    {
        try {
            Stat stat = cf.checkExists().forPath("/NettyRpc");
            if (stat == null) {
                String path = cf.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath("/NettyRpc");
                logger.info("success create zookeeper rootNode:path{}", path);
            }
        }catch (Exception e) {
            logger.error("",e);
        }
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
     * 创建服务接口节点
     * @param interfaceName
     */
    public void createInterfaceNode(String interfaceName)
    {
        Stat stat=null;
        try {
            stat=cf.checkExists().forPath("/NettyRpc/"+interfaceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(stat!=null)
        {
            logger.info("you have registed this service");
            return;
        }
        try {
            String path=cf.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath("/NettyRpc"+"/"+interfaceName);
            logger.info("success create zookeeper interface Node:path{}",path);
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    /**
     * 创建服务地址节点
     * @param interfaceName
     * @param serverAddress
     */
    public void createInterfaceAddressNode(String interfaceName,String serverAddress)
    {
        createInterfaceNode(interfaceName);
        Stat stat=null;
        try {
           stat=cf.checkExists().forPath("/NettyRpc/"+interfaceName+"/"+serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(stat!=null)
        {
            logger.info("you have registed this service");
            return;
        }
        try {
            String path=cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .forPath("/NettyRpc"+"/"+interfaceName+"/"+serverAddress);
            logger.info("success create interface address node:path{}",path);
        } catch (Exception e) {
            logger.error("",e);
        }
    }


}
