package com.zcl.nettyRpc.codecUntil;

/**
 *Kryo编码器
 * Created by zhangchenlin on 17/5/16.
 */
public class KryoEncoder extends MessageEncoder {
    public KryoEncoder(MessageCodecUntil until)
    {
        super(until);
    }
}
