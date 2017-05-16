package com.zcl.nettyRpc.codecUntil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * rpc消息解码器
 * Created by zhangchenlin on 17/5/16.
 */
public class MessageDecoder extends ByteToMessageDecoder {
    private static final Logger logger= LoggerFactory.getLogger(MessageDecoder.class);
    final public static int MESSAGE_LENGTH=MessageCodecUntil.MESSAGE_LENGTH;

    MessageCodecUntil until=null;
    public MessageDecoder(MessageCodecUntil until)
    {
        this.until=until;
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {

        if(in.readableBytes()<MESSAGE_LENGTH){
            return;
        }
        in.markReaderIndex();

        int messagelength=in.readInt();

        if(messagelength>in.readableBytes())
        {
            in.resetReaderIndex();
            return;
        }else {
            byte[] message=new byte[messagelength];
            in.readBytes(message);
            try {
                Object obj=until.decode(message);
                list.add(obj);
            }catch (Exception e)
            {
                logger.error(e.getMessage());
            }


        }
    }
}
