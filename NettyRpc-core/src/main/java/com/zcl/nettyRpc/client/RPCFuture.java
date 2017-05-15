package com.zcl.nettyRpc.client;

import com.zcl.nettyRpc.Exception.RPCTimeoutException;
import com.zcl.nettyRpc.Exception.ResponseException;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * RPCFuture
 * Created by 626hp on 2017/5/13.
 */
public class RPCFuture implements Future<Object> {

    private static final Logger logger= LoggerFactory.getLogger(RPCFuture.class);

    public CountDownLatch countDownLatch;

    private Request request;
    private Response response;
    private long startTime;

    private AsyncRPCCallback callback;

    //同步返回类
    public RPCFuture(Request request)
    {
        countDownLatch=new CountDownLatch(1);
        this.request=request;
        this.startTime=System.currentTimeMillis();
    }

    //异步返回类
    public RPCFuture(Request request,AsyncRPCCallback callback)
    {
        countDownLatch=new CountDownLatch(1);
        this.request=request;
        this.callback=callback;
        this.startTime=System.currentTimeMillis();
    }



    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * 阻塞方法
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response.getResult();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
       boolean awaitSuccess=false;
       awaitSuccess=countDownLatch.await(timeout,unit);
       if(!awaitSuccess) {
           throw new RPCTimeoutException();
       }
       long useTime=System.currentTimeMillis()-startTime;
        logger.info("request id:{} class: {} method:{} useTime {}ms",request.getRequestId(),request.getClassName()
        ,useTime);
        return response.getResult();
    }

    public void done(Response response)
    {
        this.response=response;
        countDownLatch.countDown();
        //处理异步消息
        if(callback!=null)
        {
            if(!response.isError())
            {
                callback.success(response.getResult());
            }else
            {
                callback.fail(new ResponseException(response.getError()));
            }
        }


        long time=System.currentTimeMillis()-startTime;
        logger.info("requestId:{}has been done,response:{},consumetime:{}",request.getRequestId(),response,time);

    }




}
