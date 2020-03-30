package com.sll.common.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@Configuration
public class DistributedLockTest {


    @Autowired
    JedisPool jedisPool;

    @Scheduled(fixedDelay = 5000)
    public void test(){

        Jedis resource = jedisPool.getResource();
        String uuid = UUID.randomUUID().toString();
        //加锁
        boolean b = RedisDistributedLock.tryGetDistributedLock(resource, "key", uuid, 100000);

        if (b){
            System.out.println("加锁成功");
        }

        //解锁
        boolean key = RedisDistributedLock.releaseDistributedLock(resource, "key", uuid);

        if (key){
            System.out.println("解锁成功");
        }
        resource.close();
    }

}
