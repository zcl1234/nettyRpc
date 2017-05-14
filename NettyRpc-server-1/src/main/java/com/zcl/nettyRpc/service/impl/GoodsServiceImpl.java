package com.zcl.nettyRpc.service.impl;

import com.zcl.nettyRpc.Entity.Goods;
import com.zcl.nettyRpc.serivce.GoodsService;

/**
 * Created by 626hp on 2017/5/14.
 */
public class GoodsServiceImpl implements GoodsService {


    @Override
    public Goods getGoods(String title) {
        Goods goods=new Goods(title,1000);
        return goods;
    }
}
