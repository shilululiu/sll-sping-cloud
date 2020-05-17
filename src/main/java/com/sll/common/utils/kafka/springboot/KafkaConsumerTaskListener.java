package com.sll.common.utils.kafka.springboot;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class KafkaConsumerTaskListener {

    private static final Logger log= LoggerFactory.getLogger(KafkaConsumerTaskListener.class);

    @Autowired
    private KafkaListenerEndpointRegistry registry;



    @Bean("MyKafkaConsumerTaskListener")
    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(KafkaConsumerFactory.consumerFactory());
        //禁止自动启动
        container.setAutoStartup(false);
        return container;
    }

    @KafkaListener(id = "durable", topics = "topic.quick.durable",containerFactory = "KafkaConsumerTaskListener")
    public void durableListener(String data) {
        //这里做数据持久化的操作
        log.info("topic.quick.durable receive : " + data);
    }


    //定时器，每天凌晨0点开启监听
    @Scheduled(cron = "0 0 0 * * ?")
    public void startListener() {
        log.info("开启监听");
        //判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer("durable").isRunning()) {
            registry.getListenerContainer("durable").start();
        }
        registry.getListenerContainer("durable").resume();
    }

    //定时器，每天早上10点关闭监听
    @Scheduled(cron = "0 0 10 * * ?")
    public void shutDownListener() {
        log.info("关闭监听");
        registry.getListenerContainer("durable").pause();
    }



}

