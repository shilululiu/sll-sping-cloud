package com.sll.common.utils.kafka.springboot;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerListener {




    //ConcurrentKafkaListenerContainerFactory为创建Kafka监听器的工程类，这里只配置了消费者
    //@KafkaListener(id = "batch",clientIdPrefix ="batch",topics = {"sll"},containerFactory = "kafkaListenerContainerFactory")
    //containerFactory  设置kafka监听类   不加单个默认使用   多个加
    @Bean("MyKafkaConsumerListener")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(KafkaConsumerFactory.consumerFactory());
        return factory;
    }


    //*******************************************************
    //这里使用@KafkaListener注解的topicPartitions属性监听不同的partition分区。
    //@TopicPartition：topic--需要监听的Topic的名称，partitions --需要监听Topic的分区id，
    //partitionOffsets --可以设置从某个偏移量开始监听
    //@PartitionOffset：partition --分区Id，非数组，initialOffset --初始偏移量
    @KafkaListener(id = "batchWithPartition",clientIdPrefix = "bwp",containerFactory = "KafkaConsumerListener",
            topicPartitions = {
                    @TopicPartition(topic = "topic.quick.batch.partition",partitions = {"1","3"}),
                    @TopicPartition(topic = "topic.quick.batch.partition",partitions = {"0","4"},
                            partitionOffsets = @PartitionOffset(partition = "2",initialOffset = "100"))
            }
    )
    public void batchListenerWithPartition(List<String> data) {
        for (String s : data) {
            System.out.println(s);
        }
    }





















}
