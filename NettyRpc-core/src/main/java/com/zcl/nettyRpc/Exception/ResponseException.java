package com.zcl.nettyRpc.Exception;

/**
 * Created by zhangchenlin on 17/5/15.
 */
public class ResponseException extends RuntimeException{
    public ResponseException(String msg)
    {
        super(msg);
    }

}
