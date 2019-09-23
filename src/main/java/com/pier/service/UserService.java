package com.pier.service;

import com.pier.bean.User;

import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/3/30 13:35
 */
public interface UserService {
    User getUser(String username, String password);

    List<User> findUsers(String userName);

    int insertUser(User user);

    List<User> findUsersByAccount(Double account);
}
