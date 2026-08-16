package com.bjsxt.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 延迟消息消费者，与 RocketMqDemoController 的延迟发送示例对应。
 */
@Component
@RocketMQMessageListener(
        topic = "demo-delay-topic",
        selectorExpression = "delay-tag",
        consumerGroup = "demo-delay-consumer-group"
)
public class DelayMessageConsumer implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        LOGGER.info("延迟消息消费者收到消息：{}", message);
    }
}
