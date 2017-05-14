package com.zcl.nettyRpc.serivce;


import com.zcl.nettyRpc.Entity.User;

/**
 * @author yingjun
 */
public interface UserService {

    public User getUser(String phone);

    public User updateUser(User user);

}
