package testSubscribe;

import com.zcl.nettyRpc.Entity.Goods;
import com.zcl.nettyRpc.Entity.Order;
import com.zcl.nettyRpc.Entity.User;
import com.zcl.nettyRpc.client.AsyncRPCCallback;
import com.zcl.nettyRpc.client.RPCFuture;
import com.zcl.nettyRpc.client.RpcClient;
import com.zcl.nettyRpc.proxy.AsyncRpcProxy;
import com.zcl.nettyRpc.serivce.GoodsService;
import com.zcl.nettyRpc.serivce.OrderService;
import com.zcl.nettyRpc.serivce.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * 客户端测试
 * Created by 626hp on 2017/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-client.xml")
public class subcribe {
    private static  final Logger logger= LoggerFactory.getLogger(subcribe.class);
    @Autowired
    private RpcClient rpcClient;
    @Test
    public void test()
    {
        /*
        GoodsService goodsService=RpcClient.createProxy(GoodsService.class);
        Goods goods=goodsService.getGoods("18912343");
        logger.info("goods information:{}",goods);
        */
    }
    @Test
    public void test2()
    {
        GoodsService goodsService=RpcClient.createProxy(GoodsService.class);
        Goods goods=goodsService.getGoods("1891233");
        logger.info("goods information:{}",goods);
    }

    @Test
    public void test3()
    {
        OrderService orderService=RpcClient.createProxy(OrderService.class);
        Order order=orderService.getOrder("zhangsan");
        List<Order> orders=orderService.getOrderList(23);
        logger.info("========order:{}",order);
        logger.info("========orders:{}",orders);
    }
    @Test
    public void test4()
    {
        UserService userService=RpcClient.createProxy(UserService.class);
        User user=userService.getUser("1293423");
        User useupdate=userService.updateUser(user);
        logger.info("user:{}",user);
        logger.info("user updated:{}",useupdate);
    }

    /**
     * 负载均衡测试
     */
    @Test
    public void test5()
    {
        for(int i=0;i<10;i++)
        {
            GoodsService goodsService=RpcClient.createProxy(GoodsService.class);
            Goods good=goodsService.getGoods("balance test"+i);
            logger.info("get goods:{}",good.getTitle());
        }
    }

    /**
     * 异步调用测试
     */
    @Test
    public void test6() throws InterruptedException {
        AsyncRpcProxy asyncRpcProxy=new AsyncRpcProxy(UserService.class);
        asyncRpcProxy.call("getUser", new AsyncRPCCallback() {
            @Override
            public void success(Object result) {
                logger.info("result:{}",result);
            }

            @Override
            public void fail(Exception e) {
                logger.error(e.getMessage());
            }
        },"181019375753");



        asyncRpcProxy.call("updateUser", new AsyncRPCCallback() {
            @Override
            public void success(Object result) {
                logger.info("user updated:{}",result);
            }

            @Override
            public void fail(Exception e) {
                logger.error(e.getMessage());
            }
        },new User(1,"zhangsna","updated user"));

        //以为是异步调用，如果不等待一点时间，程序将马上结束，那么无法看到结果
        Thread.sleep(5000);
    }


}
