package com.bjsxt.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 同步消息消费者，与 RocketMqDemoController 的同步发送示例对应。
 */
@Component
@RocketMQMessageListener(
        topic = "demo-sync-topic",
        selectorExpression = "sync-tag",
        consumerGroup = "demo-sync-consumer-group"
)
public class SyncMessageConsumer implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        LOGGER.info("同步消息消费者收到消息：{}", message);
    }
}
