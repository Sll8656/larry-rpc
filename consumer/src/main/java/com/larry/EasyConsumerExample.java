package com.larry;

import com.larry.model.User;
import com.larry.proxy.ServiceProxyFactory;
import com.larry.service.UserService;

public class EasyConsumerExample {
    public static void main(String[] args) {
        //获取UserService的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("shaolongling");
        //调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
