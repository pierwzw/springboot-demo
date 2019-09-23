package com.pier.controller;

import com.pier.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author zhongweiwu
 * @date 2019/4/2 18:29
 */
@Slf4j
@RestController
public class EmailController {


    @Autowired
    private MailUtil mailUtil;

    @GetMapping("/mail")
    public String sendMail() {

        try {
            mailUtil.sendMail("501311328@qq.com", "123", UUID.randomUUID().toString().substring(0, 6));
            log.info("send mail succeed");
        } catch (Exception e) {
            return "发送失败!";
        }
        return "发送成功!";
    }
}