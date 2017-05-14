package com.zcl.nettyRpc.codec;

import com.zcl.nettyRpc.untils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * Created by zhangchenlin on 17/5/12.
 */
public class RpcDecoder extends ByteToMessageDecoder{

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass)
    {
        this.genericClass=genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {

        if(byteBuf.readableBytes()<4)
        {
            return;
        }
        byteBuf.markReaderIndex();
        int datalength=byteBuf.readInt();
        if(datalength>byteBuf.readableBytes())
        {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data=new byte[datalength];
        byteBuf.readBytes(data);
        Object object= SerializationUtil.deserialize(data,genericClass);
        list.add(object);
    }
}
