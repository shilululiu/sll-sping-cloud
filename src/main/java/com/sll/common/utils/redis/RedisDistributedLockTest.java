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
   /* //@Scheduled(fixedDelay = 500000)
    public void test(){
        long time = new Date().getTime();
        ExecutorService fixPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            fixPool.execute(getThread(i));
        }
        long time2 = new Date().getTime();
        System.out.println("************************");
    }
*/

    public  static   int count =20;
    public static void main(String[] args) {

        long time = new Date().getTime();
        ExecutorService fixPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            fixPool.execute(getThread(i));
        }
        long time2 = new Date().getTime();
        System.out.println("************************");
        fixPool.shutdown();
    }




    private static   Runnable getThread(final int i) {

        return new Runnable() {
            @Override
            public void run() {
                Jedis resource = new Jedis("127.0.0.1",6379);
                JedisLock jedisLock = RedisDistributedLockUtil.get(resource);
               try {
                    jedisLock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //获得锁
                  //  System.out.println(i+"获得锁");

                    if (count!=15){
                        System.out.println(i+"买了一个");
                        count = count-1;
                    }
                    System.out.println("商品还有"+count);


                }catch (Exception e){
                    e.printStackTrace();
                }
                //    System.out.println(i+"释放锁");
                     //释放锁
                    jedisLock.release();
                    if(resource!=null){
                       resource.close();
                    }

            }


            };
    }





}
