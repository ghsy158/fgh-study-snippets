package com.thread.producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * �����ߺ������ߣ������ź�����ʵ��
 *
 * @author fgh
 * @since 2019/4/15 11:25
 */
public class Producer4 {

    private static Integer count = 0;

    //����3���ź���
    final Semaphore notFull = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        Producer4 producer1 = new Producer4();
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
                    notFull.acquire();
                    mutex.acquire();
                    count++;
                    System.out.println(Thread.currentThread().getName() + "����������,Ŀǰ�ܹ���" + count + "��");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mutex.release();
                    notEmpty.release();
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
                    notEmpty.acquire();
                    mutex.acquire();
                    count--;
                    System.out.println(Thread.currentThread().getName() + "�߳������ˣ�Ŀǰ�ܹ���" + count + "��");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mutex.release();
                    notFull.release();
                }

            }
        }
    }

}
