package com.fgh.alg.classic;

import java.util.Stack;

/**
 * ���ַ�ʽ�����ӡ��������
 *
 * @author fgh
 * @since 2019/4/17 15:20
 */
public class ReverseNode {

    /**
     * ����ջ���Ƚ��������
     *
     * @param node
     */
    public void reverseNode1(Node node) {
        System.out.println("��ת֮ǰ");
        Stack<Node> stack = new Stack<>();
        while (node != null) {
            System.out.print(node.value + "===>");
            stack.push(node);
            node = node.next;
        }

        System.out.println("");

        System.out.println("��ת֮��");
        while (!stack.empty()) {
            System.out.print(stack.pop().value + "===>");
        }
    }


    /**
     * ����ͷ�巨��������
     *
     * @param node
     */
    public void headInsertReverse(Node head) {
        if (head == null) return;
        //���շ�ת֮���Node
        Node node;

        Node pre = head;
        Node cur = head.next;
        Node next;

        while (cur != null) {
            next = cur.next;

            //�����ͷ�巨
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        head.next = null;
        node = pre;

        //����������
        while (node != null) {
            System.out.println(node.value);
            node = node.next;

        }
    }

    /**
     * �ݹ�ʵ��
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
