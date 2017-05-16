package com.zcl.nettyRpc.codecUntil;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 消息编解码接口
 * Created by zhangchenlin on 17/5/16.
 */
public interface MessageCodecUntil {
    final public static int MESSAGE_LENGTH=4;
    public void encode(final ByteBuf out,final Object message) throws IOException;
    public Object decode(byte[] body) throws IOException;
}
