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
            //TODO UNDESTAND
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
