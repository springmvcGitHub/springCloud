package com.example.test.testDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test1 {

    private static BlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(5);

    private static BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(5);

//    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < 6; i++) {
//            arrayBlockingQueue.put(""+i);
//        }
//        System.out.print(arrayBlockingQueue.size());
//    }
}
