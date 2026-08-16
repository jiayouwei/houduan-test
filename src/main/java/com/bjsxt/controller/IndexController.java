/**
 * Copyright (C),武汉尚学堂 2020-10-21
 * FileName: IndexController
 * Author:   武汉尚学堂【雷哥】
 * Date:     2020/10/21 10:22
 */
package com.bjsxt.controller;

import com.bjsxt.dto.UserDTO;
import com.bjsxt.service.UserService;
import com.bjsxt.util.RedisUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

/**
 * <功能简要> <br>
 * <>
 *
 * @Author 武汉尚学堂【雷哥】 
 * @createTime 2020/10/21 10:22
 * @since 1.0.0
 */
@RestController
@RequestMapping("index")
public class IndexController {

    private static final String USERS_CACHE_KEY = "users:all";
    private static final String MESSAGE_DESTINATION = "demo-topic:demo-tag";

    private final UserService userService;
    private final RedisUtil redisUtil;
    private final RocketMQTemplate rocketMQTemplate;

    public IndexController(UserService userService, RedisUtil redisUtil,
                           RocketMQTemplate rocketMQTemplate) {
        this.userService = userService;
        this.redisUtil = redisUtil;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @GetMapping("hello")
    public String hello(){
        System.out.println("测试解决冲突rrrr");
          System.out.println("保留");
        return "hello SpringBoot00wetwet004545twet";
    }

    @GetMapping("rocketmq/send")
    public String sendRocketMqMessage(@RequestParam(defaultValue = "hello RocketMQ") String message) {
        rocketMQTemplate.send(MESSAGE_DESTINATION,
                MessageBuilder.withPayload(message).build());
        return "消息发送成功：" + message;
    }

    @GetMapping("users")
    @SuppressWarnings("unchecked")
    public List<UserDTO> users() {
        Object cachedUsers = redisUtil.get(USERS_CACHE_KEY);
        if (cachedUsers instanceof List) {
            return (List<UserDTO>) cachedUsers;
        }

        List<UserDTO> users = userService.listUsers();
        redisUtil.set(USERS_CACHE_KEY, users, Duration.ofMinutes(10));
        return users;
    }
}
