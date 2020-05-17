package com.sll.common.utils.kafka.consumer.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerFilterListener {

    private static final Logger log= LoggerFactory.getLogger(KafkaConsumerTaskListener.class);



    @Bean("MyKafkaConsumerFilterListener")
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(MyKafkaConsumerFactory.consumerFactory());
        //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy((ConsumerRecord consumerRecord)->{
            long data = Long.parseLong((String) consumerRecord.value());
            log.info("filterContainerFactory filter : "+data);
            if (data % 2 == 0) {
                return false;
            }
            //返回true将会被丢弃
            return true;
        });
        return factory;
    }

   /* @KafkaListener(id = "filterCons", topics = "topic.quick.filter",containerFactory = "KafkaConsumerFilterListener")
    public void filterListener(String data) {
        //这里做数据持久化的操作
        log.error("topic.quick.filter receive : " + data);
    }*/
}