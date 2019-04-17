package com.fgh.alg.queue;

/**
 * 基于链表实现的队列
 *
 * @author fgh
 * @since 2019/4/17 18:58
 */
public class QueueBasedOnLinkedList {

    private Node head;
    private Node tail;

    /**
     * 入队
     *
     * @param value
     */
    public void enqueue(String value) {
        Node newNode = new Node(value, null);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = tail.next;
        }
    }

    /**
     * 出队
     *
     * @return
     */
    public String dequeue() {
        if (head == null) return null;
        String value = head.data;
        head = head.next;
        if (head == null)
            tail = null;
        return value;
    }

    public void printAll() {
        Node p = head;
        while (p != null) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    private static class Node {
        private String data;
        private Node next;

        public Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }

        public String getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        QueueBasedOnLinkedList queue = new QueueBasedOnLinkedList();
//        Node node4 = new Node("1",null);
//        Node node3 = new Node("1",node4);
//        Node node2 = new Node("1",node3);
//        Node node1 = new Node("1",node2);
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");

        queue.printAll();

        String item = queue.dequeue();
        System.out.println("出队==" + item);
        queue.printAll();
        item = queue.dequeue();
        System.out.println("出队==" + item);
        queue.printAll();
    }

}
