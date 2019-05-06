package com.thread;

/**
 * @author fgh
 * @since 2019/3/27 14:25
 */
public class PrintABC {
    public static class PrintThread implements  Runnable{

        private String name;//线程的名称
        private  Object prev;//上一个线程
        private Object self;//当前线程

        private PrintThread(String name,Object prev,Object self){
            this.name = name;
            this.prev = prev;
            this.self = self;
        }
        @Override
        public void run() {
            int count = 10;
            while (count> 0){
                synchronized (prev){
                    synchronized (self){
                        System.out.println(name);
                        count --;
                        self.notifyAll();
                    }

                    try {
                        prev.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        PrintThread threadA = new PrintThread("A",c,a);
        PrintThread threadB = new PrintThread("B",a,b);
        PrintThread threadC = new PrintThread("C",b,c);
        new Thread(threadA).start();
        Thread.sleep(10);
        new Thread(threadB).start();
        Thread.sleep(20);
        new Thread(threadC).start();
        Thread.sleep(20);


    }
}
