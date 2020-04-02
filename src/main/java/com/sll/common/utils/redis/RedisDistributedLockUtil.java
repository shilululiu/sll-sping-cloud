package com.sll.common.utils.redis;


import com.github.jedis.lock.JedisLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

public class RedisDistributedLockUtil {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;

    private static final String LOCK_KEY  = "lockKey";
    private static final Integer LOCK_TIME = 2000;

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




    public  static  void  lock(Jedis jedis,String key,int i) throws InterruptedException {
        JedisLock jedisLock = new JedisLock(jedis,key,i,i);
        jedisLock.acquire();
    }

    public static  void  unlock(Jedis jedis,String key,int i) throws InterruptedException {
        JedisLock jedisLock = new JedisLock(jedis,key,i,i);
        jedisLock.release();
    }











}

