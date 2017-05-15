package testSubscribe;

import com.zcl.nettyRpc.client.AsyncRPCCallback;
import com.zcl.nettyRpc.proxy.AsyncRpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步发送请求的线程类，这是为了测试并发发送请求（一起发送，而不是for循环）
 * Created by zhangchenlin on 17/5/15.
 */
public class performThread implements Runnable {
    private  static final Logger logger= LoggerFactory.getLogger(performThread.class);
    private  int requestNum;

    public static AtomicInteger count=new AtomicInteger(0);

    private AtomicInteger successNum;
    private AtomicInteger failedNum;
    private CountDownLatch countDownLatch;
    private CountDownLatch countDownLatch1;
    public AsyncRpcProxy asyncRpcProxy;
   public performThread(AsyncRpcProxy asyncRpcProxy,AtomicInteger successNum,
                        AtomicInteger failedNum,CountDownLatch countDownLatch1,CountDownLatch countDownLatch,int requestNum)
   {
       this.asyncRpcProxy=asyncRpcProxy;
       this.successNum=successNum;
       this.failedNum=failedNum;
       this.countDownLatch1=countDownLatch1;
       this.countDownLatch=countDownLatch;
       this.requestNum=requestNum;
   }
    @Override
    public void run() {
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    //    long startTime=System.currentTimeMillis();
        //一起请求
        logger.info("=============send together============");
        asyncRpcProxy.call("getGoods", new AsyncRPCCallback() {
                @Override
                public void success(Object result) {
                    logger.info("get result:{}", result);
                    successNum.incrementAndGet();
                    countDownLatch.countDown();

                }

                @Override
                public void fail(Exception e) {
                    logger.error(e.getMessage());
                    failedNum.decrementAndGet();
                    countDownLatch.countDown();
                }
            }, "goods:"+count.incrementAndGet());


       // countDownLatch1.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    //    long useTime=System.currentTimeMillis()-startTime;
        /*
        logger.info("========={} requst use time{}毫秒：{}",requestNum,useTime);
        logger.info("=========success request number:{}",successNum);
        logger.info("=========failed request number:{}",failedNum);
        */
    }
}
