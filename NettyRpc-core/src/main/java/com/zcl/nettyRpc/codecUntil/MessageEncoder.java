package com.zcl.nettyRpc.codecUntil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * rpc消息编码接口
 * Created by zhangchenlin on 17/5/16.
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {
    MessageCodecUntil until=null;
    public MessageEncoder(MessageCodecUntil until)
    {
        this.until=until;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        until.encode(byteBuf,msg);
    }
}
