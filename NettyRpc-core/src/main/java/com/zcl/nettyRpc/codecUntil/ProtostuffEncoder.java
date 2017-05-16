package com.zcl.nettyRpc.codecUntil;

/**
 * protostuff编码器
 * Created by zhangchenlin on 17/5/16.
 */
public class ProtostuffEncoder extends MessageEncoder {
    public ProtostuffEncoder(MessageCodecUntil until)
    {
        super(until);
    }
}
