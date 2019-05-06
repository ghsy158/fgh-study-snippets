package com.fgh.alg.queue;

/**
 * @author fgh
 * @since 2019/4/18 11:12
 */
public class CircularQueue {

    private String[] items;
    private int n;

    private int head = 0;
    private int tail = 0;

    // 申请一个大小为capacity的数组
    public CircularQueue(int capacity) {
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
        if ((tail + 1) / n == head) return false;
        items[tail] = item;
        tail = (tail + 1) / n;
        return true;
    }


    /**
     * 出队
     *
     * @return
     */
    public String dequeue() {

    }
}
