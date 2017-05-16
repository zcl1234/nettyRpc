package com.zcl.nettyRpc.codecUntil;

/**
 * Kryo编码器
 * Created by zhangchenlin on 17/5/16.
 */
public class KryoDecoder extends MessageDecoder {
    public KryoDecoder(MessageCodecUntil until)
    {
        super(until);
    }
}
