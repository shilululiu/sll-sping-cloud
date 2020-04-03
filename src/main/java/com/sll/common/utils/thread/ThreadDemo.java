package com.sll.common.utils.thread;

import com.github.jedis.lock.JedisLock;
import com.sll.common.utils.redis.RedisDistributedLockUtil;
import redis.clients.jedis.Jedis;

/**
 * @program: sll-sping-cloud
 * @description:  多线程 继承   Thread 类
 * @author: shill12
 * @create: 2020-03-31 10:43
 **/
public class ThreadDemo extends Thread {

    public  int count =10;

    @Override
    public void run() {
        //获得锁
        Jedis resource = new Jedis("127.0.0.1",6379);
        JedisLock jedisLock = RedisDistributedLockUtil.get(resource);
        try {
            jedisLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {

            System.out.println("获得锁");


                //该代码块是为了验证效果
                System.out.println("睡眠2秒....");
                Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        }

        //释放锁
        jedisLock.release();
        System.out.println("释放锁");
        if(resource!=null){
            resource.close();
        }

    }

}
