package com.fgh.alg.stack;


import com.fgh.alg.linkedlist.LinkedListAlgo;

/**
 * 基于链表实现的栈
 *
 * @author fgh
 * @since 2019/4/12 16:29
 */
public class StackBasedLinkedList {
    private Node top;

    public void push(int value) {
        Node newNode = new Node(value, null);
        if (top == null) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
    }

    public int pop() {
        if (top == null) {
            return -1;
        }
        int value = top.data;
        top = top.next;
        return value;
    }

    public void printAll() {
        Node p = top;
        while (p != null) {
            System.out.println(p.data + " ");
            p = p.next;
        }
    }

    public static class Node {
        private int data;
        private Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        public int getData() {
            return data;
        }
    }
}
