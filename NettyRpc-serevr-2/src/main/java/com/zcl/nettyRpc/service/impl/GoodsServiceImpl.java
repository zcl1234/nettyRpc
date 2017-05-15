package com.zcl.nettyRpc.service.impl;

import com.zcl.nettyRpc.Entity.Goods;
import com.zcl.nettyRpc.serivce.GoodsService;

/**
 * Created by zhangchenlin on 17/5/15.
 */
public class GoodsServiceImpl implements GoodsService {

    @Override
    public Goods getGoods(String title) {

        Goods goods=new Goods(title,1000);
        return goods;
    }
}
