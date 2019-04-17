package com.fgh.alg.queue;

/**
 * ������ʵ�ֵĶ���
 *
 * @author fgh
 * @since 2019/4/17 18:36
 */
public class ArrayQueue {

    private String[] items;
    //�����С
    private int n;

    //��ͷ�±�
    private int head = 0;
    //��β�±�
    private int tail = 0;

    public ArrayQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    /**
     * ���
     *
     * @param item
     * @return
     */
    public boolean enqueue(String item) {
        //��������
        if (tail == n) return false;
        items[tail] = item;
        ++tail;
        return true;
    }

    /**
     * ����
     *
     * @return
     */
    public String dequeue() {
        //����Ϊ��
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
        System.out.println("����="+item);
        arrayQueue.printAll();
        item = arrayQueue.dequeue();
        System.out.println("����="+item);
        arrayQueue.printAll();
    }
}
