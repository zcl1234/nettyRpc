package com.zcl.nettyRpc.parrallel;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂
 * Created by zhangchenlin on 17/5/16.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber=new AtomicInteger(1);

    private final AtomicInteger mThreadNum=new AtomicInteger(1);

    private final String prefix;
    private final boolean daemoThread;

   private ThreadGroup threadGroup;

    public NamedThreadFactory()
    {
        this("rpcserver-threadpool-"+threadNumber.getAndIncrement(),false);
    }

    public NamedThreadFactory(String prefix)
    {
        this(prefix,false);
    }


    public NamedThreadFactory(String prefix,boolean daemo)
    {
        this.prefix= StringUtils.isNotEmpty(prefix)?prefix+"-thread-":"";
        this.daemoThread=daemo;
        SecurityManager securityManager=System.getSecurityManager();
        threadGroup=(securityManager==null)?Thread.currentThread().getThreadGroup():securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String name=prefix+mThreadNum.getAndIncrement();
        Thread thread=new Thread(threadGroup,r,name,0);//第四个参数为0表示忽略最后一个参数
        thread.setDaemon(daemoThread);
        return thread;
    }

    public ThreadGroup getThreadGroup()
    {
        return threadGroup;
    }


}
