package com.zcl.nettyRpc.codecUntil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * rpc消息序列化协议类型
 * Created by zhangchenlin on 17/5/16.
 */
public enum RpcSerializeProtocol {
    JDKSERIALIZE("protostuff"),
    KRYOSERIALIZE("kryo"),
    HESSIANSERIALIZE("hession");


    private String serializeProtocol;

    private RpcSerializeProtocol(String serializeProtocol)
    {
        this.serializeProtocol=serializeProtocol;
    }

    public String toString()
    {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public String getSerializeProtocol()
    {
        return serializeProtocol;
    }
}
