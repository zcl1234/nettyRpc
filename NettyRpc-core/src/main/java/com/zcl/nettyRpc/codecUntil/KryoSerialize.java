package com.zcl.nettyRpc.codecUntil;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Kryo序列化工具
 * Created by zhangchenlin on 17/5/16.
 */
public class KryoSerialize implements RpcSerialize {

    //kryo3.0.1版本之后才支持
    private KryoPool pool;

    public KryoSerialize(KryoPool pool)
    {
        this.pool=pool;
    }


    @Override
    public void serialize(OutputStream outputStream, Object object) throws IOException {
        Kryo kryo=pool.borrow();
        Output output=new Output(outputStream);
        kryo.writeClassAndObject(output,object);
        output.close();
        pool.release(kryo);
    }

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        Kryo kryo=pool.borrow();
        Input input=new Input(inputStream);
        Object result=kryo.readClassAndObject(input);
        input.close();
        pool.release(kryo);
        return result;
    }
}
