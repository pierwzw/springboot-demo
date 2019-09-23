package com.pier.mq.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zhongweiwu
 * @date 2019/4/2 16:41
 */
@Slf4j
@Component
public class RabbitmqConsumer {

    /**
     * @Description: 消费消息
     * @method: handleMessage
     * @Param: message
     * @return: void
     */
    @RabbitListener(queues = "DirectQueue")
    @RabbitHandler
    public void directMessage(Message message, Channel channel) throws IOException {
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
        //System.out.println(message.getPayload().toString());
        log.info("DirectConsumer directMessage:" + message.getPayload().toString());
    }

    @RabbitListener(queues = "TopicQueue")
    @RabbitHandler
    public void topicMessage(String message){
        log.info("TopicConsumer {} topicMessage :"+message);
    }

    @RabbitListener(queues = "FanoutQueue")
    @RabbitHandler
    public void fanoutMessage(String message){
        log.info("FanoutConsumer {} fanoutMessage :"+message);
    }

    @RabbitListener(queues = "HeadersQueue")
    @RabbitHandler
    public void headersMessage(Message message){
        log.info("HeadersConsumer {} headersMessage :"+message);
    }

    /**
     * @Description: 消费消息
     * @method: handleMessage
     * @Param: message
     * @return: void
     */
    /*@RabbitListener(queues = "DirectQueue")
    @RabbitHandler
    public void directMessage(String sendMessage, Channel channel, Message message) throws Exception {
        try {
            Assert.notNull(sendMessage, "sendMessage 消息体不能为NULL");
            log.info("处理MQ消息");
            // prefetchCount限制每个消费者在收到下一个确认回执前一次可以最大接受多少条消息,通过basic.qos方法设置prefetch_count=1,这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message
            channel.basicQos(1);
            log.info("DirectConsumer {} directMessage :" + message);
            // 确认消息已经消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("MQ消息处理异常，消息ID：{}，消息体:{}", message.getMessageProperties().getCorrelationId(),sendMessage,e);
            // 拒绝当前消息，并把消息返回原队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }*/
}
