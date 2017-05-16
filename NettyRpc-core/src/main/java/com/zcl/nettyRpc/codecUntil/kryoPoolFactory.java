package com.zcl.nettyRpc.codecUntil;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.zcl.nettyRpc.protocol.Request;
import com.zcl.nettyRpc.protocol.Response;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 *
 * kryo对象池工厂
 * Created by zhangchenlin on 17/5/16.
 */
public class kryoPoolFactory {
    private static kryoPoolFactory poolFactory=null;

    private KryoFactory kryoFactory=new KryoFactory() {
        @Override
        public Kryo create() {
            Kryo kryo=new Kryo();
            //开启这个选项后，相同的对象将被序列化为同一个byte[]，默认关闭，如果要支持循环引用，则必须开启
            kryo.setReferences(false);
            //把已知结构注册到kryo注册器里面，提高序列化／反序列化效率
            kryo.register(Request.class);
            kryo.register(Response.class);
            kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };

    private KryoPool pool=new KryoPool.Builder(kryoFactory).build() ;
    private kryoPoolFactory()
    {

    }
    public static KryoPool getKryoPoolInstance()
    {
        if(poolFactory==null)
        {
          synchronized (kryoPoolFactory.class)
          {
              if(poolFactory==null)
                  poolFactory=new kryoPoolFactory();
          }
        }
        return poolFactory.getPool();
    }

    public KryoPool getPool()
    {
        return pool;
    }

}
