package com.sll.common.utils.redis;



import com.github.jedis.lock.JedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class RedisDistributedLockTest {


    @Autowired
    public JedisPool jedisPool;
    //@Scheduled(fixedDelay = 5000)
    public void test(){
        long time = new Date().getTime();
        ExecutorService fixPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            fixPool.execute(getThread(i));
        }
        long time2 = new Date().getTime();
        System.out.println(time2-time);
    }

    private  Runnable getThread(final int i) {

        return new Runnable() {
            @Override
            public void run() {
                Jedis resource = jedisPool.getResource();
                JedisLock jedisLock = RedisDistributedLockUtil.get(resource);
                try {
                    jedisLock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //获得锁
                    RedisDistributedLockUtil.lock(resource,"uu",2000);
                    System.out.println(i+"获得锁");
                    if(i==2){
                        //该代码块是为了验证效果
                        System.out.println(i+"睡眠2秒....");
                        Thread.sleep(2000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    System.out.println(i+"释放锁");
                     //释放锁
                     jedisLock.release();
                    if(resource!=null){
                       resource.close();
                    }
                }
            }


            };
    }





}
