package com.larry;

import com.larry.model.User;
import com.larry.proxy.ServiceProxyFactory;
import com.larry.service.UserService;

public class ConsumerExample {
    public static void main(String[] args) {
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("larry");
        // 调⽤
        User newUser = userService.getUser(user);
        User newUser1 = userService.getUser(user);
        User newUser2 = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
//        short number = userService.getNumber();
//        System.out.println(number);
    }
}