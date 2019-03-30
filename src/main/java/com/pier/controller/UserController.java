package com.pier.controller;

import com.pier.bean.User;
import com.pier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @auther zhongweiwu
 * @date 2019/3/30 12:09
 */
@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getuser")
    public ModelAndView getUser(@RequestParam String username, @RequestParam String password){
        User user = userService.getUser(username, password);
        if (user != null){
            return new ModelAndView("index").addObject("user", user);
        }
        return new ModelAndView("error").addObject("msg", "user has not existed");
    }
}
