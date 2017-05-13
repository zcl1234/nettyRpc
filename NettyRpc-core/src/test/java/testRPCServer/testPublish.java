package testRPCServer;

import com.zcl.nettyRpc.server.RPCServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchenlin on 17/5/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-server.xml")
public class testPublish {

    @Autowired
    RPCServer rpcServer;

    @Test
    public void test()
    {

    }
}
