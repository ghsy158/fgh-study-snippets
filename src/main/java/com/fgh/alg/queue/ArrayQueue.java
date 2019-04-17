package com.fgh.alg.queue;

/**
 * 用数组实现的队列
 *
 * @author fgh
 * @since 2019/4/17 18:36
 */
public class ArrayQueue {

    private String[] items;
    //数组大小
    private int n;

    //队头下表
    private int head = 0;
    //队尾下标
    private int tail = 0;

    public ArrayQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    /**
     * 入队
     *
     * @param item
     * @return
     */
    public boolean enqueue(String item) {
        //队列满了
        if (tail == n) return false;
        items[tail] = item;
        ++tail;
        return true;
    }

    /**
     * 出队
     *
     * @return
     */
    public String dequeue() {
        //队列为空
        if (head == tail) return null;
        return items[head++];
    }

    public void printAll() {
        for (int i = head; i < tail; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue(4);
        arrayQueue.enqueue("1");
        arrayQueue.enqueue("2");
        arrayQueue.enqueue("3");
        arrayQueue.enqueue("4");

        String item = arrayQueue.dequeue();
        System.out.println("出队="+item);
        arrayQueue.printAll();
        item = arrayQueue.dequeue();
        System.out.println("出队="+item);
        arrayQueue.printAll();
    }
}
