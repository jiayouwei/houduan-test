package com.bjsxt.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 异步消息消费者，与 RocketMqDemoController 的异步发送示例对应。
 */
@Component
@RocketMQMessageListener(
        topic = "demo-async-topic",
        selectorExpression = "async-tag",
        consumerGroup = "demo-async-consumer-group"
)
public class AsyncMessageConsumer implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        LOGGER.info("异步消息消费者收到消息：{}", message);
    }
}
