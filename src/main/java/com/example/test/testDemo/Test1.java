package com.example.test.testDemo;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test1 {

    private static BlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(5);

    private static BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(5);

//    public static void main(String[] args) throws InterruptedException {
//        //test1();
//        test2();
//    }

    private static void test1() throws InterruptedException{
        for (int i = 0; i < 6; i++) {
            arrayBlockingQueue.put(""+i);
        }
        System.out.print(arrayBlockingQueue.size());
    }

    private static void test2() throws InterruptedException{
        //Set不能存放重复元素，但是由于数组可以存放null，所以set只能存放一个null，且是无序容器
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            set.add("1");
        }
        System.out.println(set.size());
    }
}
