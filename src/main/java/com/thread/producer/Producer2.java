package com.thread.producer;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.camel.EndpointConfiguration.UriFormat.Consumer;

/**
 * 生产者和消费者，ReentrantLock的实现
 *
 * @author fgh
 * @since 2019/4/15 11:25
 */
public class Producer2 {

    public static int FULL_COUNT = 10;

    private static Integer count = 0;

    private static ReentrantLock lock = new ReentrantLock();

    //创建两个条件变量，一个为缓冲区不满，一个缓冲区非空
    private static final Condition notFull = lock.newCondition();
    private static final Condition notEmpty = lock.newCondition();


    public static void main(String[] args) {
        Producer2 producer1 = new Producer2();
        new Thread(producer1.new Producer()).start();
        new Thread(producer1.new Consumer()).start();
        new Thread(producer1.new Producer()).start();
        new Thread(producer1.new Consumer()).start();
        new Thread(producer1.new Producer()).start();
        new Thread(producer1.new Consumer()).start();
        new Thread(producer1.new Producer()).start();
        new Thread(producer1.new Consumer()).start();

    }

    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    while (count == FULL_COUNT) {
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "生产者生产,目前总共有" + count + "个");
                    notEmpty.signal();
                } finally {
                    lock.unlock();
                }
            }
        }

    }
        class Consumer implements Runnable {

            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.lock();
                    try {
                        while (count == 0) {
                            try {
                                notEmpty.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        count--;
                        System.out.println(Thread.currentThread().getName() + "线程消费了，目前总共有" + count + "个");
                        notFull.signal();
                    } finally {
                        lock.unlock();
                    }

                }
            }
        }

    }
