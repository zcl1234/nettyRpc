package com.zcl.nettyRpc.codecUntil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RPC消息序列化／反序列化消息接口定义
 * Created by zhangchenlin on 17/5/16.
 */
public interface RpcSerialize {
    void serialize(OutputStream outputStream,Object object)throws IOException;

    Object deserialize(InputStream inputStream) throws IOException;
}
