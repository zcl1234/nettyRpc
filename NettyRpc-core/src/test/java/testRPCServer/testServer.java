package testRPCServer;

import com.zcl.nettyRpc.client.RpcClient;
import com.zcl.nettyRpc.server.RPCServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchenlin on 17/5/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-client.xml")
public class testServer {

    /*
    @Test
    public void testPublish()
    {
        RPCServer rpcServer=new RPCServer("localhost:2190");
        List<Class<?>> list=new ArrayList<>();
        list.add(testRPCServer.testImpl.class);
        rpcServer.publishService(list);
    }
    */
    @Test
    public void testSubcrib()
    {

    }

}
