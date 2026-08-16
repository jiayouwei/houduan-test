package com.bjsxt.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "demo-topic",
        selectorExpression = "demo-tag",
        consumerGroup = "demo-consumer-group"
)
public class DemoMessageConsumer implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        LOGGER.info("RocketMQ 消费到消息：{}", message);
    }
}
