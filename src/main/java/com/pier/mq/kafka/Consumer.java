package com.pier.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @auther zhongweiwu
 * @date 2019/4/2 17:39
 */
@Slf4j
@Component
public class Consumer {
    @KafkaListener(topics = "test")
    public void listen(ConsumerRecord<?,String> record) throws InterruptedException {
        String value = record.value();
        log.info("consume message:" + value);
    }
}
