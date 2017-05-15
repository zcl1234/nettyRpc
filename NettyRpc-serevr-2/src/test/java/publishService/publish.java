package publishService;

import com.zcl.nettyRpc.server.RPCServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 发布服务
 * Created by 626hp on 2017/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring-server.xml")
public class publish {

    @Autowired
    private RPCServer rpcServer;
    @Test
    public void test()
    {
        rpcServer.startServer();
    }
}
