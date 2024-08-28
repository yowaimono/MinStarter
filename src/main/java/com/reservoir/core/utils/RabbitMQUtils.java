package com.reservoir.core.utils;//package com.reservoir.core.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Slf4j
//@Component
//public class RabbitMQUtils {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * 发送普通消息
//     *
//     * @param exchange   交换机
//     * @param routingKey 路由键
//     * @param message    消息内容
//     */
//    public void sendMessage(String exchange, String routingKey, Object message) {
//        log.info("Sending message to exchange: {}, routingKey: {}, message: {}", exchange, routingKey, message);
//        rabbitTemplate.convertAndSend(exchange, routingKey, message);
//    }
//
//    /**
//     * 发送延迟消息
//     *
//     * @param exchange   交换机
//     * @param routingKey 路由键
//     * @param message    消息内容
//     * @param delayTime  延迟时间（毫秒）
//     */
//    public void sendDelayedMessage(String exchange, String routingKey, Object message, int delayTime) {
//        log.info("Sending delayed message to exchange: {}, routingKey: {}, message: {}, delayTime: {} ms", exchange, routingKey, message, delayTime);
//        MessageProperties messageProperties = new MessageProperties();
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("x-delay", delayTime);
//        messageProperties.setHeaders(headers);
//        Message msg = rabbitTemplate.getMessageConverter().toMessage(message, messageProperties);
//        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
//    }
//
//    /**
//     * 发送带有优先级的消息
//     *
//     * @param exchange   交换机
//     * @param routingKey 路由键
//     * @param message    消息内容
//     * @param priority   优先级（0-10）
//     */
//    public void sendPriorityMessage(String exchange, String routingKey, Object message, int priority) {
//        log.info("Sending priority message to exchange: {}, routingKey: {}, message: {}, priority: {}", exchange, routingKey, message, priority);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setPriority(priority);
//        Message msg = rabbitTemplate.getMessageConverter().toMessage(message, messageProperties);
//        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
//    }
//
//    /**
//     * 发送消息并等待确认
//     *
//     * @param exchange   交换机
//     * @param routingKey 路由键
//     * @param message    消息内容
//     * @param correlationData 相关数据（用于确认回调）
//     */
//    public void sendMessageWithConfirm(String exchange, String routingKey, Object message, CorrelationData correlationData) {
//        log.info("Sending message with confirm to exchange: {}, routingKey: {}, message: {}, correlationData: {}", exchange, routingKey, message, correlationData);
//        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
//    }
//
//    /**
//     * 接收消息
//     *
//     * @param queueName 队列名称
//     * @return 消息内容
//     */
//    public Object receiveMessage(String queueName) {
//        log.info("Receiving message from queue: {}", queueName);
//        return rabbitTemplate.receiveAndConvert(queueName);
//    }
//
//    /**
//     * 发送消息并设置消息ID
//     *
//     * @param exchange   交换机
//     * @param routingKey 路由键
//     * @param message    消息内容
//     */
//    public void sendMessageWithId(String exchange, String routingKey, Object message) {
//        log.info("Sending message with ID to exchange: {}, routingKey: {}, message: {}", exchange, routingKey, message);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setMessageId(UUID.randomUUID().toString());
//        Message msg = rabbitTemplate.getMessageConverter().toMessage(message, messageProperties);
//        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
//    }
//}