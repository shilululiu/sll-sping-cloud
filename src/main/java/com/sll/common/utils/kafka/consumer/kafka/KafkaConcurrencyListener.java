package com.sll.common.utils.kafka.consumer.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaConcurrencyListener {

    private static final Logger log= LoggerFactory.getLogger(KafkaConcurrencyListener.class);
    // Kafka集群地址
    private static final String brokerList = "localhost:9092";





    @Bean("MyKafkaConcurrencyListener")
    public ConcurrentKafkaListenerContainerFactory listenerContainer() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(MyKafkaConsumerFactory.consumerFactory());
        //设置并发量，小于或等于Topic的分区数//多个线程监听
        container.setConcurrency(5);
        //设置为批量监听
        container.setBatchListener(true);
        return container;
    }



    //@Bean
    public NewTopic batchTopic() {
        return new NewTopic("sll", 8, (short) 1);
    }


   /* @KafkaListener(id = "batch",clientIdPrefix ="batch",topics = {"sll"},containerFactory = "KafkaConcurrencyListener")
    public void batchListener(List<String> data) {
        log.info("topic.quick.batch  receive : ");
        for (String s : data) {
            log.info(  s);
        }
    }*/



}
