package com.zcl.nettyRpc.codecUntil;

/**
 * protostuff解码器
 * Created by zhangchenlin on 17/5/16.
 */
public class ProtostuffDecoder extends MessageDecoder {
    public ProtostuffDecoder(MessageCodecUntil until)
    {
        super(until);
    }
}
