package testSubscribe;

import com.zcl.nettyRpc.Entity.Goods;
import com.zcl.nettyRpc.client.RpcClient;
import com.zcl.nettyRpc.serivce.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        GoodsService goodsService=RpcClient.createProxy(GoodsService.class);
        Goods goods=goodsService.getGoods("18912343");
        logger.info("goods information:{}",goods);
    }
    @Test
    public void test2()
    {
        GoodsService goodsService=RpcClient.createProxy(GoodsService.class);
        Goods goods=goodsService.getGoods("1891233");
        logger.info("goods information:{}",goods);
    }
}
