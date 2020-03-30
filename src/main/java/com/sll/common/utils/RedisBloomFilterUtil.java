package com.sll.common.utils;


import com.google.common.base.Preconditions;
import com.sll.application.config.BloomFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

@Component
public class RedisBloomFilterUtil {

    @Autowired
    RedisTemplate redisTemplate;

   /* @Autowired
    private JedisCluster cluster;

    public RedisBloomFilter(JedisCluster jedisCluster) {
        this.cluster = jedisCluster;
    }*/

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
       // System.out.println("内容："+offset[0]+"-----"+offset[1]+"-----"+offset[2]+"-----"+offset.length);
        for (int i : offset) {
           redisTemplate.opsForValue().setBit(key, i, true);
            //cluster.setbit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
       // System.out.println("内容："+offset[0]+"-----"+offset[1]+"-----"+offset[2]+"-----"+offset.length);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(key, i)) {
           // if (!cluster.getbit(key, i)) {
                return false;
            }
        }

        return true;
    }


}
