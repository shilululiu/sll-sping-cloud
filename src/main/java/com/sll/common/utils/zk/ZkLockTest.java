package com.sll.common.utils.zk;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZkLockTest {


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

                try {
                    //获得锁
                    ZkLockUtil.get().acquire();
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
                        try {
                            ZkLockUtil.get().release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }


        };
    }




}
