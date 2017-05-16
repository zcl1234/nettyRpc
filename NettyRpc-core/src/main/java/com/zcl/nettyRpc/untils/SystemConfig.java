package com.zcl.nettyRpc.untils;

/**
 * Created by zhangchenlin on 17/5/16.
 */
public class SystemConfig {
    public static final String SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR =
                                         "com.newlandframework.rpc.parallel.rejected.policy";
    public static final String SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR =
                                               "com.newlandframework.rpc.parallel.queue";
    public static final int PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
}
