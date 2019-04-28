package com.fgh.alg.stack;

/**
 * @author fgh
 * @since 2019/4/12 16:42
 */
public class LinkedListBasedStack {

    private int size;
    private Node top;

    static Node createNode(String data, Node next) {
        return new Node(data, next);
    }

    public void clear() {
        this.top = null;
        this.size = 0;
    }

    public void push(String data) {
        Node node = createNode(data, this.top);
        this.top = node;
        this.size++;
    }

    public String pop() {
        Node popNode = this.top;
        if (popNode == null) {
            System.out.println("stack is empty");
            return null;
        }
        this.top = popNode.next;
        if (this.size > 0) {
            this.size--;
        }
        return popNode.data;
    }

    public String getTopData() {
        if (this.top == null) {
            return null;
        }
        return this.top.data;
    }

    public int size() {
        return this.size;
    }

    public void print() {
        System.out.println("print stack:");
        Node currentNode = this.top;
        while (currentNode != null) {
            String data = currentNode.data;
            System.out.println("data=" + data);
            currentNode = currentNode.next;
        }
    }

    public static class Node {

        private String data;
        private Node next;

        public Node(String data) {
            this(data, null);
        }

        public Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData() {
            return this.data;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getNext() {
            return this.next;
        }
    }
}
