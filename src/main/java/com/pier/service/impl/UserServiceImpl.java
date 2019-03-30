package com.pier.service.impl;

import com.pier.bean.User;
import com.pier.dao.UserDao;
import com.pier.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @auther zhongweiwu
 * @date 2019/3/30 13:37
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }
}
