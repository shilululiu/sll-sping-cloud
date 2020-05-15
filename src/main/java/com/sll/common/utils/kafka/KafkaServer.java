package com.sll.common.utils.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaServer {

    // Kafka集群地址
     private static final String brokerList = "localhost:9092";
    // 主题名称-之前已经创建
     private static final String topic = "sll";

    public static void main(String[] args) {
        //构建参数
        Properties properties = initConfig();
        //构建生产者
        KafkaProducer<String, String> producer = new KafkaProducer<> (properties);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", "你好");
        //发送
        producer.send(record);
        //关闭
        producer.close();


    }



    public static Properties initConfig(){

        Properties  properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokerList);
        // 设置key序列化器
        // properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置值序列化器
        // properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        return  properties;
    }

}
