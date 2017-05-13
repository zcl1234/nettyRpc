package com.zcl.nettyRpc.codec;

import com.zcl.nettyRpc.untils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *编码器
 * Created by zhangchenlin on 17/5/12.
 */
public class RpcEncoder extends MessageToByteEncoder{
    private Class<?> genericClass;
    public RpcEncoder(Class<?> genericClass)
    {
        this.genericClass=genericClass;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(object))
        {
            byte[] data= SerializationUtil.serialize(object);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }

    }
}
