package com.sll.common.utils.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @program: sll-sping-cloud
 * @description: 线程池
 * @author: shill12
 * @create: 2020-03-31 10:55
 **/

public class TestThreadPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<List<Integer>>> ints = new ArrayList<>();
        for(int i = 0 ; i < 5; i ++) {
            Future<List<Integer>> future = executorService.submit(new Callable<List<Integer>>() {
                @Override
                public List<Integer> call() throws Exception {
                    boolean flag = false;
                    System.out.println(Thread.currentThread().getName()+"  ");
                    List<Integer> lists = new ArrayList<>();
                    for(int i  = 3 ; i < 100 ; i ++) {
                        flag = false;
                        for(int j = 2; j <= Math.sqrt(i) ; j++) {
                            if(i % j == 0) {
                                flag = true;
                                break;
                            }
                        }
                        if(flag == false) {
                            lists.add(i);
                        }
                    }
                    return lists;
                }
            });
            ints.add(future);
        }

        for (Future<List<Integer>> future : ints) {
            System.out.println(future.get());
        }
    }
}

class ThreadPoolDemo {

}
