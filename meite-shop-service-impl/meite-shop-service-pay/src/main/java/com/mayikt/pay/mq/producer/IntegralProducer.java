package com.mayikt.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liujinqiang
 * @create 2021-02-26 22:19
 */
@Component
@Slf4j
public class IntegralProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void send(JSONObject jsonObject) {
        String jsonString = jsonObject.toJSONString();
        log.info("jsonString:" + jsonString);
        String paymentId = jsonObject.getString("paymentId");
        //封装消息
        MessageBuilderSupport<Message> message = MessageBuilder.withBody(jsonString.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(paymentId);
        //构建回调返回的数据
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend("integral_exchange_name", "integralRoutinKey", message, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String jsonString = correlationData.getId();
        log.info("消息id:" + jsonString);
        if (ack) {
            log.info(">>>使用MQ消息确认机制确保消息一定要投递到MQ中成功");
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //投递失败重试
        send(jsonObject);
        log.info(">>>使用MQ消息确认机制投递到MQ中失败");
    }
}
