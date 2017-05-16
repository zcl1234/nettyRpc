package com.zcl.nettyRpc.parrallel;

import com.zcl.nettyRpc.parrallel.policy.*;
import com.zcl.nettyRpc.untils.SystemConfig;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 自定义线程池
 * Created by zhangchenlin on 17/5/16.
 */
public class RpcThreadPool {
    private static final Logger logger= LoggerFactory.getLogger(RpcThreadPool.class);

    private static RejectedExecutionHandler createPolicy()
    {
        RejectedPolicyType rejectedPolicyType=RejectedPolicyType.fromString(
                System.getProperty(SystemConfig.SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR,"AbortPolicy"));

        switch (rejectedPolicyType) {
            case BLOCKING_POLICY:
                return new BlockingPolicy();
            case CALLER_RUNS_POLICY:
                return new CallerRunsPolicy();
            case ABORT_POLICY:
                return new AbortPolicy();
            case REJECTED_POLICY:
                return new RejectedPolicy();
            case DISCARDED_POLICY:
                return new DiscardedPolicy();
        }

        return null;
    }

    private static BlockingQueue<Runnable> createBlockingQueue(int queues)
    {
        BlockingQueueType queueType = BlockingQueueType.fromString(System.getProperty(SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR, "LinkedBlockingQueue"));

        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(SystemConfig.PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }

        return null;
    }

        public static Executor getExecutor(int threads,int queues)
        {
            logger.info("ThreadPool core[threads:"+threads+",queues:"+queues+"]");
            String name="RpcThreadpool";
            ThreadPoolExecutor executor=new ThreadPoolExecutor(threads,threads,0,TimeUnit.MILLISECONDS,
                    createBlockingQueue(queues),new NamedThreadFactory(name,true),createPolicy());
            return executor;
        }



}
