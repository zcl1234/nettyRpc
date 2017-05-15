package com.zcl.nettyRpc.service.impl;

import com.zcl.nettyRpc.Entity.User;
import com.zcl.nettyRpc.serivce.UserService;

/**
 * Created by zhangchenlin on 17/5/15.
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(String phone) {
        User user=new User(1,"zhangsan","18101937573");
        return user;
    }

    @Override
    public User updateUser(User user) {
        user.setId(2);
        user.setName("new name");
        user.setPhone("new phoneNumber");
        return user;
    }
}
