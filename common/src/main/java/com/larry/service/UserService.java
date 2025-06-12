package com.larry.service;

import com.larry.model.User;

public interface UserService {
    /**
     * 新⽅法 - 获取数字
     */
//    default short getNumber() {
//        return 1;
//    }
    /**
     * 获取⽤户
     *
     * @param user
     * @return
     */
    User getUser(User user);

}
