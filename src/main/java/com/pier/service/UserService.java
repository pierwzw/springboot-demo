package com.pier.service;

import com.pier.bean.User;

/**
 * @auther zhongweiwu
 * @date 2019/3/30 13:35
 */
public interface UserService {
    User getUser(String username, String password);
}
