package com.pier.controller;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

/**
 * @auther zhongweiwu
 * @date 2019/4/2 15:52
 */
@Slf4j
@RestController
public class RabbitmqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Description: 发送消息
     * 1.交换机
     * 2.key
     * 3.消息
     * 4.消息ID
     * rabbitTemplate.send(message);   发消息;参数对象为org.springframework.amqp.core.Message
     * rabbitTemplate.convertAndSend(message); 转换并发送消息;将参数对象转换为org.springframework.amqp.core.Message后发送,消费者不能有返回值
     * rabbitTemplate.convertSendAndReceive(message) //转换并发送消息,且等待消息者返回响应消息.消费者可以有返回值
     * @method: handleMessage
     * @Param: message
     * @return: void
     */
    @GetMapping("/directSend")
    public void directSend() {
        String message="direct 发送消息";
        rabbitTemplate.convertAndSend("DirectExchange","DirectKey",
                message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @GetMapping("/topicSend")
    public void topicSend() {
        String message="topic 发送消息";
        rabbitTemplate.convertAndSend("TopicExchange","Topic.Key",
                message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @GetMapping("/fanoutSend")
    public void fanoutSend() {
        String message="fanout 发送消息";
        rabbitTemplate.convertAndSend("FanoutExchange","",message, new CorrelationData(UUID.randomUUID().toString()));
    }

    @GetMapping("/headersSend")
    public void headersSend(){
        String msg="headers 发送消息";
        MessageProperties properties = new MessageProperties();
        properties.setHeader("headers1","value1");
        properties.setHeader("headers2","value2");
        Message message = new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend("HeadersExchange","",message, new CorrelationData(UUID.randomUUID().toString()));
    }
}
