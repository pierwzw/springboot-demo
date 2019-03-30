package com.pier.dao;

import com.pier.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @auther zhongweiwu
 * @date 2019/3/30 13:39
 */
public interface UserDao {
    User getUser(@Param("username") String username, @Param("password") String password);
}
