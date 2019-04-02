package com.pier.controller;

import com.pier.bean.User;
import com.pier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @auther zhongweiwu
 * @date 2019/3/30 12:09
 */
@Slf4j
@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/getuser")
    public ModelAndView getUser(@RequestParam String username, @RequestParam String password){
        String key = "user:" + username + ":" + password;
        User user = (User)redisTemplate.opsForValue().get(key);
        if(user == null){
            user = userService.getUser(username, password);
            if (user == null){
                log.info("has no this user!");
                return new ModelAndView("error").addObject("msg", "user has not existed");
            }
            redisTemplate.opsForValue().set(key, user);
        }
        return new ModelAndView("index").addObject("user", user);
    }
}
