package com.sll.common.utils.kafka.consumer.controller;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {

    @KafkaListener(id = "sll",clientIdPrefix ="sll",topics = {"sll"},containerFactory = "MyKafkaConsumerListener")
    public void kafkaLListener(List<String> data){
        for (String datum : data) {
            System.out.println(datum);
        }
    }
}
