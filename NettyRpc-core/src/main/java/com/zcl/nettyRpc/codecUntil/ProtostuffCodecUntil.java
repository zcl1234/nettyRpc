package com.zcl.nettyRpc.codecUntil;

import com.zcl.nettyRpc.untils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * prostuff 编解码工具类
 * Created by zhangchenlin on 17/5/16.
 */
public class ProtostuffCodecUntil implements MessageCodecUntil {
    private static final Logger logger= LoggerFactory.getLogger(ProtostuffCodecUntil.class);

    private Class<?> clazz;
    public ProtostuffCodecUntil(Class<?> clazz)
    {
        this.clazz=clazz;
    }

    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
     //  logger.info("==========================message:{}",message);
        if(clazz.isInstance(message)) {
            byte[] body = SerializationUtil.serialize(message);
            out.writeInt(body.length);
            out.writeBytes(body);
        }


    }

    @Override
    public Object decode(byte[] body) throws IOException {

        Object object=SerializationUtil.deserialize(body,clazz);

     //   logger.info("====================object:{}",object);

        return object;
    }
}
