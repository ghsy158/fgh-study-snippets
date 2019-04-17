package com.thread.producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * �����ߺ������ߣ�ArrayBlockingQueue��ʵ��
 *
 * @author fgh
 * @since 2019/4/15 11:25
 */
public class Producer3 {

    private static Integer count = 0;

    private BlockingQueue blockingQueue = new ArrayBlockingQueue(10);

    public static void main(String[] args) {
        Producer3 producer1 = new Producer3();
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
                try {
                    blockingQueue.put(1);
                    count++;
                    System.out.println(Thread.currentThread().getName() + "����������,Ŀǰ�ܹ���" + count + "��");
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                try {
                    blockingQueue.take();
                    count--;
                    System.out.println(Thread.currentThread().getName() + "�߳������ˣ�Ŀǰ�ܹ���" + count + "��");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
