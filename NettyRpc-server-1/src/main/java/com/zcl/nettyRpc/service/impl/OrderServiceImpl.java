package com.zcl.nettyRpc.service.impl;

import com.zcl.nettyRpc.Entity.Order;
import com.zcl.nettyRpc.serivce.OrderService;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangchenlin on 17/5/15.
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public Order getOrder(String uuid) {
        Order order=new Order(uuid, 1,"first order",1000);
        return order;
    }

    @Override
    public List<Order> getOrderList(long userId) {
        List<Order> orders=new LinkedList<>();
        Order order1=new Order(UUID.randomUUID().toString(),userId,"1th order",1000);
        Order order2=new Order(UUID.randomUUID().toString(),userId,"2th order",2000);
        orders.add(order1);
        orders.add(order2);
        return orders;
    }
}
