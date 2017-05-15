package com.zcl.nettyRpc.client;

/**
 * 异步回调接口
 * Created by zhangchenlin on 17/5/15.
 */
public interface AsyncRPCCallback {
     void success(Object result);
     void fail(Exception e);
}
