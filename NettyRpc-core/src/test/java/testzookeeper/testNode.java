package testzookeeper;

import com.zcl.nettyRpc.registry.ServiceDiscovery;
import com.zcl.nettyRpc.registry.ServiceRegistry;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * zookeeper节点监控的测试
 * Created by zhangchenlin on 17/5/12.
 */
public class testNode {

    /**
     * 测试服务注册功能
     */
    @Test
    public void testRegister()
    {
        new ServiceRegistry();
    }

    /**
     * 测试服务发现功能
     */
    @Test
    public void testDiscovry()
    {
        List<String> list=new LinkedList<>();
        list.add("interface1");
        list.add("interface2");
        list.add("interface3");

        try {
            new ServiceDiscovery("172.16.34.136:2181",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
