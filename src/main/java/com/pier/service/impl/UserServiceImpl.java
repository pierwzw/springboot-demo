package com.pier.service.impl;

import com.pier.bean.User;
import com.pier.dao.UserDao;
import com.pier.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/3/30 13:37
 */
@Service
@Transactional(value = "txManager")  // 这个错不用管 其实这个txManager可以省略，只有在存在多个事务管理器的情况下才会需要
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    /*@Cacheable(value = "users", key = "'username:' + #username")*/
    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }

    @Override
    public List<User> findUsers(String userName) {
        return userDao.findUsers(userName);
    }

    //@Transactional(value = "txManager")
    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public List<User> findUsersByAccount(Double account) {
        return userDao.findUsersByAccount(account);
    }
}
