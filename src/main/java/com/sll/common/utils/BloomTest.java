package com.sll.common.utils;


import com.google.common.hash.Funnels;
import com.sll.application.config.BloomFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.Charset;

@Configuration
public class BloomTest {

    @Autowired
    RedisBloomFilterUtil redisBloomFilter;

    private static BloomFilterHelper<CharSequence> bloomFilterHelper;
    //@Scheduled(fixedDelay = 5000)
    public  void test(){

            bloomFilterHelper = new BloomFilterHelper<>(Funnels.stringFunnel(Charset.defaultCharset()), 10000, 0.01);
           //******* Redis集群测试布隆方法*********
            int j = 0;
            /*for (int i = 0; i < 9000 ; i++) {
                redisBloomFilter.addByBloomFilter(bloomFilterHelper, "bloom2", i+"");
            }*/
        System.out.println("开始");
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10000 ; i++) {
                boolean result = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, "bloom2", i+"");
                if (!result) {
                    j++;
                }
            }
        long l2 = System.currentTimeMillis();
        System.out.println((l2-l)/1000+"秒");
        System.out.println((l2-l)/1000/10000+"秒");

            System.out.println(j + "个数据不存在过滤器里");
        }

}
