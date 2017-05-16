package com.zcl.nettyRpc.codecUntil;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo编解码工具类
 * Created by zhangchenlin on 17/5/16.
 */
public class KryoCodecUntil implements MessageCodecUntil {
    private static final Logger logger= LoggerFactory.getLogger(KryoCodecUntil.class);

    private KryoPool pool;
    private static Closer closer=Closer.create();

    public KryoCodecUntil(KryoPool pool)
    {
        this.pool=pool;
    }
    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
        try {
           // logger.info("=================message:{}",message);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            KryoSerialize serialize = new KryoSerialize(pool);
            serialize.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            out.writeInt(body.length);
            out.writeBytes(body);
        }finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
          // logger.info("=======================================body:{]",body);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize serialize = new KryoSerialize(pool);
            Object obj = serialize.deserialize(byteArrayInputStream);
            //logger.info("======================================decode:{}",obj);
            return obj;
        }finally {
            closer.close();
        }
    }
}
