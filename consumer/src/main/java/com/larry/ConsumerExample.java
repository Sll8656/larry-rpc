package com.larry;

import com.larry.config.RpcConfig;
import com.larry.model.User;
import com.larry.proxy.ServiceProxyFactory;
import com.larry.service.UserService;
import com.larry.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        System.out.println(userService == null);
        User user = new User();
        user.setName("larry");
        // 调⽤
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        short number = userService.getNumber();
        System.out.println(number);
    }
}