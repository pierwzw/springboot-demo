package com.pier.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zhongweiwu
 * @date 2019/4/2 17:27
 */
@Slf4j
@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public String send(String name) throws InterruptedException {
        int i=1;
        while(true){
            log.info("start send kafka message:" + name + "--" + i++ );
            kafkaTemplate.send("test", name + "--" + i);
            Thread.sleep(500);
        }
        //return name;
    }
}