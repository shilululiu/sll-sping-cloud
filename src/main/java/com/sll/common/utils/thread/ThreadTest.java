package com.sll.common.utils.thread;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @program: sll-sping-cloud
 * @description:
 * @author: shill12
 * @create: 2020-03-31 10:41
 **/
public class ThreadTest {

   /* public static void main(String[] args) throws ExecutionException, InterruptedException {

        //继承Thread类
        for (int i = 0; i < 10; i++) {
            ThreadDemo threadDemo = new ThreadDemo();
            threadDemo.start();
        }

        //实现Runnable 接口
        RunnableDemo runnableDemo = new RunnableDemo();
        new Thread(runnableDemo).start();

        //实现Callable接口
        CallableDemo callableDemo = new CallableDemo();
        FutureTask futureTask = new FutureTask<>(callableDemo);
        new Thread(futureTask).start();
        List<Integer> lists = (List<Integer>)futureTask.get(); //获取返回值
        for (Integer integer : lists) {
            System.out.print(integer + "  ");
        }





    }*/
}
