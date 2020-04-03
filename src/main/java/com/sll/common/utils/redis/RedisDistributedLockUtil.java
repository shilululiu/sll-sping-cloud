package com.sll.common.utils.redis;


import com.github.jedis.lock.JedisLock;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisDistributedLockUtil {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;

    private static final String LOCK_KEY  = "lockKey";
    private static final Integer LOCK_TIME = 10000;

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        SetParams setParams = new SetParams();
        //即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作;
        setParams.nx();
        //key加一个过期的设置 expireTime 时间;
        setParams.px(expireTime);
        String result =jedis.set(lockKey, requestId,setParams);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }



    public static  JedisLock get(Jedis jedis){
        JedisLock jedisLock = new JedisLock(jedis,LOCK_KEY,LOCK_TIME,LOCK_TIME);
       return jedisLock;
    }


    /**
     * Redisson实现分布式锁，官方推荐
     * @return
     * @throws InterruptedException
     */
      public  static RedissonRedLock getredLock() throws InterruptedException {
        //可以向多个redis实例加锁，避免master 宕机

          Config config1=new Config();
          config1.useSingleServer().setAddress("redis://127.0.0.1:6379");
          RedissonClient redisson1 =  Redisson.create(config1);

        /*  Config config2=new Config();
          config2.useSingleServer().setAddress("redis://127.0.0.1:6378");
          RedissonClient redisson2 =  Redisson.create(config2);


          Config config3=new Config();
          config3.useSingleServer().setAddress("redis://127.0.0.1:6377");
          RedissonClient redisson3 =  Redisson.create(config3);*/

          RLock rLock1=redisson1.getLock("key");
        /*  RLock rLock2=redisson2.getLock("key");
          RLock rLock3=redisson3.getLock("key");*/

          // 向3个redis实例尝试加锁
          RedissonRedLock redLock = new RedissonRedLock(rLock1/*,rLock2,rLock3*/);
          //解锁加锁操作
          // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
          //redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
          //redLock.unlock();
          return redLock;
      }









    public  static  void  lock(Jedis jedis,String key,int i) throws InterruptedException {
        JedisLock jedisLock = new JedisLock(jedis,key,i,i);
        jedisLock.acquire();
    }

    public static  void  unlock(Jedis jedis,String key,int i) throws InterruptedException {
        JedisLock jedisLock = new JedisLock(jedis,key,i,i);
        jedisLock.release();
    }











}

