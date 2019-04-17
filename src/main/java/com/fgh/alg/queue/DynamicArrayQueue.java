package com.fgh.alg.queue;

/**
 * 用数组实现的队列
 *
 * @author fgh
 * @since 2019/4/17 18:36
 */
public class DynamicArrayQueue {

    private String[] items;
    //数组大小
    private int n;

    //队头下表
    private int head = 0;
    //队尾下标
    private int tail = 0;

    public DynamicArrayQueue(int capacity) {
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
        if (tail == n) {
            if (head == 0) return false;
            //数组的删除操作会导致数组中的数据不连续
            //数据搬移
            for (int i = head; i < tail; i++) {
                items[i - head] = items[i];
            }
            //搬移完之后更新head和tail
            tail -= head;
            head = 0;
        }
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
        DynamicArrayQueue arrayQueue = new DynamicArrayQueue(4);
        arrayQueue.enqueue("1");
        arrayQueue.enqueue("2");
        arrayQueue.enqueue("3");
        arrayQueue.enqueue("4");

        String item = arrayQueue.dequeue();
        System.out.println("出队=" + item);
        arrayQueue.printAll();
        item = arrayQueue.dequeue();
        System.out.println("出队=" + item);
        arrayQueue.printAll();
    }
}
