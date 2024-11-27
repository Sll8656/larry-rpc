package com.larry;

import com.larry.model.User;

public class UserServiceImpl {
    public User getUser(User user) {
        System.out.println("用户名:" + user.getName());
        return user;
    }
}
