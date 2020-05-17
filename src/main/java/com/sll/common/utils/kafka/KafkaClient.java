package com.sll.common.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class KafkaClient {
    // Kafka集群地址
    private static final String brokerList = "localhost:9092";
    // 主题名称
    private static final String topic = "sll";
    // 消费组
    private static final String groupId = "sll-group";


    public static void main(String[] args) {
        //构建参数
        Properties properties = initConfig();
        //构建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<> (properties);
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("接收到"+record.key()+record.value());
            }
        }
    }


    public static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,brokerList);
        //设置key序列化器
        //properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //设置值序列化器
        //properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG,"client.sll.demo");
        return  properties;
    }



    //linxu  bat 改sh
    //win 创建主题 kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic sll
    //查看主题  kafka-topics.bat --list --zookeeper localhost:2181
    //查看主题详情 kafka-topics.bat --zookeeper localhost:2181 --describe --topic sll
    //删除主题 kafka-topics.bat --delete --zookeeper localhost:2181 --topic sll2
    // .\bin\windows\kafka-server-start.bat .\config\server.properties





}
