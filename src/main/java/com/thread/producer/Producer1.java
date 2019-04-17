package com.thread.producer;

import java.util.concurrent.locks.Lock;

/**
 * �����ߺ������ߣ�wait()��notify()��ʵ��
 *
 * @author fgh
 * @since 2019/4/15 11:25
 */
public class Producer1 {

    public static int FULL_COUNT = 10;

    private static Integer count = 0;

    private static Object lock = new Object();


    public static void main(String[] args) {
        Producer1 producer1 = new Producer1();
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
                synchronized (lock) {
                    while (count == FULL_COUNT) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "����������,Ŀǰ�ܹ���" + count + "��");
                    lock.notifyAll();
                }
            }
        }
    }


    class Consumer implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (count == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + "�߳������ˣ�Ŀǰ�ܹ���" + count + "��");
                    lock.notifyAll();
                }
            }
        }
    }

}
