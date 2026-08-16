package com.bjsxt.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * RocketMQ 四种常用消息发送示例。
 *
 * 每个示例使用独立的 topic 和 tag，便于通过对应消费者观察消息行为。
 */
@RestController
@RequestMapping("rocketmq/demo")
public class RocketMqDemoController {

    private static final String SYNC_DESTINATION = "demo-sync-topic:sync-tag";
    private static final String ASYNC_DESTINATION = "demo-async-topic:async-tag";
    private static final String ORDERLY_DESTINATION = "demo-orderly-topic:orderly-tag";
    private static final String DELAY_DESTINATION = "demo-delay-topic:delay-tag";

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMqDemoController(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 同步消息：send 方法会等待 Broker 返回发送结果后再返回。
     */
    @GetMapping("/sync")
    public String sendSync(@RequestParam(defaultValue = "同步消息") String message) {
        rocketMQTemplate.syncSend(SYNC_DESTINATION,
                MessageBuilder.withPayload(message).build());
        return "同步消息发送成功：" + message;
    }

    /**
     * 异步消息：发送请求后立即返回，发送结果在回调线程中处理。
     */
    @GetMapping("/async")
    public String sendAsync(@RequestParam(defaultValue = "异步消息") String message) {
        CompletableFuture.runAsync(() -> rocketMQTemplate.asyncSend(ASYNC_DESTINATION,
                MessageBuilder.withPayload(message).build(), new org.apache.rocketmq.client.producer.SendCallback() {
                    @Override
                    public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                        System.out.println("RocketMQ 异步消息发送成功：" + sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.err.println("RocketMQ 异步消息发送失败：" + throwable.getMessage());
                    }
                }));
        return "异步消息已提交：" + message;
    }

    /**
     * 顺序消息：相同 hashKey 的消息会发送到同一个队列，消费者按队列顺序消费。
     */
    @GetMapping("/orderly")
    public String sendOrderly(@RequestParam(defaultValue = "订单顺序消息") String message,
                              @RequestParam(defaultValue = "order-1001") String hashKey) {
        rocketMQTemplate.syncSendOrderly(ORDERLY_DESTINATION,
                MessageBuilder.withPayload(message).build(), hashKey);
        return "顺序消息发送成功：" + message;
    }

    /**
     * 延迟消息：delayLevel=3 表示 RocketMQ 预置的延迟级别，通常约为 10 秒。
     */
    @GetMapping("/delay")
    public String sendDelay(@RequestParam(defaultValue = "延迟消息") String message,
                            @RequestParam(defaultValue = "3") int delayLevel) {
        rocketMQTemplate.syncSend(DELAY_DESTINATION,
                MessageBuilder.withPayload(message).build(), delayLevel);
        return "延迟消息发送成功：" + message;
    }
}
