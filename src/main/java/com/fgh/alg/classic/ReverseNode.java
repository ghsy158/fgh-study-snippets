package com.fgh.alg.classic;

import java.util.Stack;

/**
 * 三种方式反向打印单向链表
 *
 * @author fgh
 * @since 2019/4/17 15:20
 */
public class ReverseNode {

    /**
     * 利用栈的先进后出特性
     *
     * @param node
     */
    public void reverseNode1(Node node) {
        System.out.println("翻转之前");
        Stack<Node> stack = new Stack<>();
        while (node != null) {
            System.out.print(node.value + "===>");
            stack.push(node);
            node = node.next;
        }

        System.out.println("");

        System.out.println("翻转之后");
        while (!stack.empty()) {
            System.out.print(stack.pop().value + "===>");
        }
    }


    /**
     * 利用头插法插入链表
     *
     * @param node
     */
    public void headInsertReverse(Node head) {
        if (head == null) return;
        //最终翻转之后的Node
        Node node;

        Node pre = head;
        Node cur = head.next;
        Node next;

        while (cur != null) {
            next = cur.next;

            //链表的头插法
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        head.next = null;
        node = pre;

        //遍历新链表
        while (node != null) {
            System.out.println(node.value);
            node = node.next;

        }
    }

    /**
     * 递归实现
     *
     * @param node
     */
    public void recNode(Node node) {
        if (node == null) return;
        if (node.next != null) {
            recNode(node.next);
        }
        System.out.print(node.value + "===>");
    }

    public static class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }
}
