package com.zcl.nettyRpc.serivce;


import com.zcl.nettyRpc.Entity.Order;

import java.util.List;

/**
 *
 * @author yingjun
 */
public interface OrderService {

    public Order getOrder(String uuid);

    public List<Order> getOrderList(long userId);



}
