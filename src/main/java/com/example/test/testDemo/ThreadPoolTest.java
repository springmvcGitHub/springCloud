package com.example.test.testDemo;

import java.util.concurrent.*;

public class ThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executor1 = Executors.newFixedThreadPool(3);
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5);
//        ExecutorService executor2 = Executors.newCachedThreadPool();
        for (int i = 0; i < 7; i++) {
            System.out.println("M1------------"+((ThreadPoolExecutor)executor1).getTaskCount());
            Runnable runnable = new Task();

            int count2 = ((ThreadPoolExecutor)executor1).getPoolSize();

            if(count2 == 3){
                workQueue.add(runnable);
            }else{
                executor1.submit(runnable);
            }
            System.out.println("M2------------"+count2);
            System.out.println("M3------------"+workQueue.size());
        }
        executor1.shutdown();
    }
}

class Task implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}