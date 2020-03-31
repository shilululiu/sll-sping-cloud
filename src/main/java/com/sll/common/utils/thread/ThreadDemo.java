package com.sll.common.utils.thread;

/**
 * @program: sll-sping-cloud
 * @description:  多线程 继承   Thread 类
 * @author: shill12
 * @create: 2020-03-31 10:43
 **/
public class ThreadDemo extends Thread {

    @Override
    public void run() {
        boolean flag = false;
        for(int i  = 3 ; i < 100 ; i ++) {
            flag = false;
            for(int j = 2; j <= Math.sqrt(i) ; j++) {
                if(i % j == 0) {
                    flag = true;
                    break;
                }
            }
            if(flag == false) {
                System.out.print(i+"  ");
            }
        }
    }

}
