package com.sll.common.utils.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecutorTest {


       Lock lock = new ReentrantLock();


       public void  get (){


           ExecutorService mCachelThreadPool = Executors.newCachedThreadPool();




       }



}
