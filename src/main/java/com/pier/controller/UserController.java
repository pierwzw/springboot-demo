package com.pier.controller;

import com.pier.bean.User;
import com.pier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author zhongweiwu
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
        // 即使是@RestController还是能返回view而不是json,与官方文档是说的
        // ‘只要添加的有Jackson2依赖，Spring Boot应用中的任何@RestController默认都会渲染为JSON响应’，不太一样
        return new ModelAndView("index").addObject("user", user);
    }

    @GetMapping("/insert")
    @ResponseBody
    public String insertUser(User user) {
        if (log.isDebugEnabled()){
            log.debug("user:" + user);
        }
        try{
            int row = userService.insertUser(user);
        }catch(Exception e){
            return "error:" + e.getMessage();
        }
        return "success";
    }
}
