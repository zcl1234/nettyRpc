package com.zcl.nettyRpc.codecUntil;

import io.netty.channel.ChannelPipeline;

/**
 * RPC消息序列化协议选择器接口
 * Created by zhangchenlin on 17/5/16.
 */
public interface RpcSerializeFrame {
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
