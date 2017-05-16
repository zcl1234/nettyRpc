package testSubscribe;


import com.zcl.nettyRpc.client.AsyncRPCCallback;
import com.zcl.nettyRpc.proxy.AsyncRpcProxy;
import com.zcl.nettyRpc.serivce.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能测试
 * Created by zhangchenlin on 17/5/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-client.xml")
public class performance {

    public static final Logger logger= LoggerFactory.getLogger(performance.class);

    private static final int requestNum=10000;

    private AtomicInteger successNum=new AtomicInteger(0);
    private AtomicInteger failedNum=new AtomicInteger(0);

    /**
     * 并非是一起发送请求
     */
    @Test
    public void testPerformance()
    {
        CountDownLatch countDownLatch=new CountDownLatch(requestNum);
        AsyncRpcProxy asyncRpcProxy=new AsyncRpcProxy(GoodsService.class);
        long startTime=System.currentTimeMillis();
        for(int i=0;i<requestNum;i++) {
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
            }, "goods:" + i);

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long useTime=System.currentTimeMillis()-startTime;
        logger.info("========={} requst use time{}毫秒：{}",requestNum,useTime);
        logger.info("=========success request number:{}",successNum);
        logger.info("=========failed request number:{}",failedNum);
    }

    /**
     * 这个测试是为了测试多个客户端一起请求服务器的情况
     * @throws InterruptedException
     */
    @Test
    public void testParrelsend() throws InterruptedException {
        int requestNum=2000;
        AtomicInteger successNum=new AtomicInteger(0);
        AtomicInteger failedNum=new AtomicInteger(0);
        CountDownLatch countDownLatch1=new CountDownLatch(1);
        CountDownLatch countDownLatch=new CountDownLatch(requestNum);
        AsyncRpcProxy asyncRpcProxy=new AsyncRpcProxy(GoodsService.class);
        for(int i=0;i<requestNum;i++)
        {
            Thread t=new Thread(new performThread(asyncRpcProxy,successNum,failedNum,countDownLatch1,countDownLatch,requestNum));
            t.start();
        }


        countDownLatch1.countDown();
        long startime=System.currentTimeMillis();

        countDownLatch.await();
        long useTime=System.currentTimeMillis()-startime;
        logger.info("========={} requst use time{}毫秒：{}",requestNum,useTime);
        logger.info("=========success request number:{}",successNum);
        logger.info("=========failed request number:{}",failedNum);


    }




}
