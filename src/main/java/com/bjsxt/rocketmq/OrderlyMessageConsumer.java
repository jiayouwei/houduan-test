package com.bjsxt.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 顺序消息消费者，与发送端使用同一个订单 hashKey 的消息会在同一个队列中按顺序处理。
 */
@Component
@RocketMQMessageListener(
        topic = "demo-orderly-topic",
        selectorExpression = "orderly-tag",
        consumerGroup = "demo-orderly-consumer-group",
        consumeMode = org.apache.rocketmq.spring.annotation.ConsumeMode.ORDERLY
)
public class OrderlyMessageConsumer implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderlyMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        LOGGER.info("顺序消息消费者按顺序收到消息：{}", message);
    }
}
