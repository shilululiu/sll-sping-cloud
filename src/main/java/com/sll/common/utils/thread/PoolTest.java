package com.sll.common.utils.thread;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @program: sll-sping-cloud
 * @description: 常见线程池
 * @author: shill12
 * @create: 2020-03-31 11:09
 **/
public class PoolTest {
   
    
    
    
    /*/**
     * @Author shill12 
     * @Description 固定大小的线程池，可以指定线程池的大小，该线程池corePoolSize和maximumPoolSize相等，阻塞队列使用的是LinkedBlockingQueue，大小为整数最大值。该线程池中的线程数量始终不变，当有新任务提交时，线程池中有空闲线程则会立即执行，如果没有，则会暂存到阻塞队列。对于固定大小的线程池，不存在线程数量的变化。同时使用无界的LinkedBlockingQueue来存放执行的任务。当任务提交十分频繁的时候，LinkedBlockingQueue迅速增大，存在着耗尽系统资源的问题。而且在线程池空闲时，即线程池中没有可运行任务时，它也不会释放工作线程，还会占用一定的系统资源，需要shutdown。
     * @Date 11:11 2020/3/31
     * @Param 
     * @return 
     */
    public static ExecutorService newFixedThreadPool(int var0) {
        return new ThreadPoolExecutor(var0, var0, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    public static ExecutorService newFixedThreadPool(int var0, ThreadFactory var1) {
        return new ThreadPoolExecutor(var0, var0, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), var1);
    }
    
    
    
    /*/**
     * @Author shill12 
     * @Description 缓存线程池，缓存的线程默认存活60秒。线程的核心池corePoolSize大小为0，核心池最大为Integer.MAX_VALUE,阻塞队列使用的是SynchronousQueue。是一个直接提交的阻塞队列，    他总会迫使线程池增加新的线程去执行新的任务。在没有任务执行时，当线程的空闲时间超过keepAliveTime（60秒），则工作线程将会终止被回收，当提交新任务时，如果没有空闲线程，则创建新线程执行任务，会导致一定的系统开销。如果同时又大量任务被提交，而且任务执行的时间不是特别快，那么线程池便会新增出等量的线程池处理任务，这很可能会很快耗尽系统的资源。
     * @Date 11:12 2020/3/31
     * @Param 
     * @return 
     */
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory var0) {
        return new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), var0);
    }




    /*/**
     * @Author shill12 
     * @Description 单个线程线程池，只有一个线程的线程池，阻塞队列使用的是LinkedBlockingQueue,若有多余的任务提交到线程池中，则会被暂存到阻塞队列，待空闲时再去执行。按照先入先出的顺序执行任务。
     * @Date 11:18 2020/3/31
     * @Param 
     * @return 
     */
    public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory var0) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), var0);
    }


    /*/**
     * @Author shill12 
     * @Description 定时线程池，该线程池可用于周期性地去执行任务，通常用于周期性的同步数据。scheduleAtFixedRate:是以固定的频率去执行任务，周期是指每次执行任务成功执行之间的间隔。schedultWithFixedDelay:是以固定的延时去执行任务，延时是指上一次执行成功之后和下一次开始执行的之前的时间。
     * @Date 11:20 2020/3/31
     * @Param 
     * @return 
     */
    public static ScheduledExecutorService newScheduledThreadPool(int var0) {
        return new ScheduledThreadPoolExecutor(var0);
    }

    public static ScheduledExecutorService newScheduledThreadPool(int var0, ThreadFactory var1) {
        return new ScheduledThreadPoolExecutor(var0, var1);
    }



    //线程池实例*****************************************************分界线



    //固定大小的线程池newFixedThreadPool实例：

    private static Runnable getThread(final int i) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i+"    ");
            }
        };
    }

    public static void main(String args[]) {
        long time = new Date().getTime();
        ExecutorService fixPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000; i++) {
            fixPool.execute(getThread(i));
        }
        fixPool.shutdown();
        long time2 = new Date().getTime();
        System.out.println(time2-time);
    }


    //单个线程线程池newCachedThreadPool实例：

    /*private static Runnable getThread(final int i){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (Exception e){

                }
                System.out.println(i);
            }
        };
    }

    public static  void main(String args[]){
        ExecutorService cachePool = Executors.newCachedThreadPool();
        for (int i=1;i<=10;i++){
            cachePool.execute(getThread(i));
        }
    }

   // 这里没用调用shutDown方法，这里可以发现过60秒之后，会自动释放资源。
    */


    //缓存线程池newSingleThreadExecutor实例:


   /* private static Runnable getThread(final int i){
        return new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        };
    }

    public static void main(String args[]) throws InterruptedException {
        ExecutorService singPool = Executors.newSingleThreadExecutor();
        for (int i=0;i<10;i++){
            singPool.execute(getThread(i));
        }
        singPool.shutdown();
        这里需要注意一点，newSingleThreadExecutor和newFixedThreadPool一样，在线程池中没有任务时可执行，也不会释放系统资源的，所以需要shudown。
        */


    //定时线程池newScheduledThreadPool实例:
    /*public static void main(String args[]) {

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    System.out.println(Thread.currentThread().getId() + "执行了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }*/






}
