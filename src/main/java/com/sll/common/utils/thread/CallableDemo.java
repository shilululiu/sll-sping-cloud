package com.sll.common.utils.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @program: sll-sping-cloud
 * @description: 实现Callable
 * @author: shill12
 * @create: 2020-03-31 10:50
 **/
public class CallableDemo implements Callable<List<Integer>> {

    @Override
    public List<Integer> call() throws Exception {
        boolean flag = false;
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

}
