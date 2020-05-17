package com.sll.common.utils.kafka.springboot;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Component
public class KafkaProducer {



    //kafkaTemplate实现了Kafka发送接收等功能  @Primary  多个bean  优先使用这个
    @Bean
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate template = new KafkaTemplate<String, String>(producerFactory());
        //设置默认主题  defaultKafkaTemplate.sendDefault("I`m send msg to default topic");
        template.setDefaultTopic("sll");
        return template;
    }


    //根据senderProps填写的参数创建生产者工厂
    @Bean
    public ProducerFactory<String, String> producerFactory() {

        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(senderProps());
        //开始事务
        factory.transactionCapable();
        //设置事务id前缀
        factory.setTransactionIdPrefix("tran-");
        return factory ;
    }



    //生产者配置
    private Map<String, Object> senderProps (){
        Map<String, Object> props = new HashMap<>();
        //连接地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        //控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        //生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
        //键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //分区器
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,DefinePartitioner.class);
        //拦截器
        //props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,ProducerInterceptorPrefix.class);
        return props;
    }


    //************************************************
    //事务
    @Bean
    public KafkaTransactionManager transactionManager(ProducerFactory producerFactory) {
        KafkaTransactionManager manager = new KafkaTransactionManager(producerFactory);
        return manager;
    }



    //***********************************************

    @Autowired
    KafkaTemplate kafkaTemplate;
    //同步调用
    public void testSyncSend() throws ExecutionException, InterruptedException {
        kafkaTemplate.send("topic.quick.demo", "test sync send message").get();
    }
    //当send方法耗时大于get方法所设定的参数时会抛出一个超时异常，但需要注意，这里仅抛出异常，消息还是会发送成功的。这里的测试方法设置send耗时必须小于 一微秒
    public void testTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        kafkaTemplate.send("topic.quick.demo", "test send message timeout").get(1, TimeUnit.MICROSECONDS);
    }


    /**
     * #kafka-producer配置，官网=>https://kafka.apache.org/documentation/#producerconfigs
     * #集群地址
     * spring.kafka.producer.bootstrap-servers=192.168.199.128:9092,192.168.199.128:9093,192.168.199.128:9094
     * #指定创建信息nio-buffer缓冲区大小约1M
     * spring.kafka.producer.buffer-memory=1024000
     * #累计约1M条就发发送，必须小于缓冲区大小，否则报错无法分配内存（减少IO次数，过大则延时高，瞬间IO大）
     * spring.kafka.producer.batch-size=1024000
     * #默认0ms立即发送，不修改则上两条规则相当于无效（这个属性时个map列表，producer的其它配置也配置在这里，详细↑官网，这些配置会注入给KafkaProperties这个配置bean中，供#spring自动配置kafkaTemplate这个对象时使用）
     * spring.kafka.producer.properties.linger.ms=1000
     * #发送确认机制:acks=all或-1：leader会等待所有ISR中的follower同步完成的ack才commit(保证ISR副本都有数据leader才commit，吞吐率降低),acks=0：partition leader不会等待任何ISR中副本的commit（可能会有数据丢失，吞吐高），acks=1 kafka会把这条消息写到本地日志文件中
     * spring.kafka.producer.acks=1
     * #发送失败重试次数
     * spring.kafka.producer.retries=3
     * #kafkaTempalte默认的StringDeserializer序列化器（可指定json系列化器org.springframework.kafka.support.serializer.JsonSerializer，但同时消费的也要随着配置对应反序列化器org.springframework.kafka.support.serializer.JsonDeSerializer）
     * spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.ByteArraySerializer
     *
     * #****************************************************************************
     * #kafka-consumer配置，官网=>https://kafka.apache.org/documentation/#producerconfigs
     * spring.kafka.consumer.bootstrap-servers=192.168.199.128:9092,192.168.199.128:9093,192.168.199.128:9094
     * #消费组ID
     * spring.kafka.consumer.group-id=test-group
     * #当未初始化消费组偏移量或没找到是怎么办？自动偏移量重置到最早的earlist（这个值会导致新加入的消费者去消费较久以前最开始的大量信息）,最新的latest（新消费者消费最新消息）,还是none或其它值报错
     * spring.kafka.consumer.auto-offset-reset=latest
     * #一次最大拉取多少条消息，太多处理消息压力大，太少则IO过于频繁；这个配置需要额外培养一个批量工厂bean，并在@KafkaListener注解指定这个批量工厂{@Link https://www.jianshu.com/p/5370fff55cff}
     * spring.kafka.consumer.max-poll-records=10
     * #自动提交consumer已消费的消息offset周期,周期过大，重启后可能重复消费较多已消费但offset未提交的消息,kafka scala程序有个定时任务来提交offset
     * spring.kafka.consumer.auto-commit-interval=1000ms
     * #仅在不开启上述周期性自动确认，配置其他的ack-mode才有效，如下累计10次消费才进行一次commit以修改消息消费者在该partition的offset
     * spring.kafka.consumer.enable-auto-commit=false
     * #计数模式，还是计时模式，手动ack模式等，配合ack-count一起配置
     * spring.kafka.listener.ack-mode=count_time
     * spring.kafka.listener.ack-count=10
     * #消费者的其它属性，类似producer，也是一个map
     * #spring.kafka.consumer.properties.XXX=YYY
     * #value即消息的反系列化方式，默认StringDeserializer
     * spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer
     *
     * #****************************************************************************
     * #springcloud-stream-kafka基本配置
     * spring.cloud.stream.kafka.binder.brokers=192.168.199.128:9092,192.168.199.128:9093,192.168.199.128:9094
     * #最小分区数
     * spring.cloud.stream.kafka.binder.minPartitionCount=3
     * #副本数
     * spring.cloud.stream.kafka.binder.replication-factor=1
     * #是否自动创建topic
     * spring.cloud.stream.kafka.binder.autoCreateTopics=true
     * #是否自动分区
     * spring.cloud.stream.kafka.binder.autoAddPartitions=true
     * #绑定消息发送通道到topic——test2
     * spring.cloud.stream.bindings.output.destination=test2
     * #绑定消息接收通道到topic——test1
     * spring.cloud.stream.bindings.input.destination=test1
     * #自定义消息发送通道myoutput1
     * spring.cloud.stream.bindings.myoutput1.destination=test2
     */




}
