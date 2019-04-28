package com.fgh.alg.linkedlist;

import scala.collection.concurrent.SNode;

import java.util.Scanner;

/**
 * @author fgh
 * @since 2019/4/11 17:46
 */
public class LRUBaseLinkedList<T> {

    /**
     * Ĭ����������
     */
    private static final Integer DEFAULT_CAPACITY = 10;

    /**
     * ͷ���
     */
    private SNode<T> headNode;

    /**
     * ������
     */
    private Integer length;

    /**
     * ��������
     */
    private Integer capcacity;


    public LRUBaseLinkedList() {
        this.headNode = new SNode<>();
        this.capcacity = DEFAULT_CAPACITY;
        this.length = 0;
    }

    public LRUBaseLinkedList(Integer capacity) {
        this.headNode = new SNode<>();
        this.capcacity = capacity;
        this.length = 0;
    }

    /**
     * ���Ԫ��
     *
     * @param data
     */
    public void add(T data) {
        SNode preNode = findPreNode(data);

//        �����д��ڣ�ɾ��ԭ���ݣ��ڲ��뵽�����ͷ��
        if (preNode != null) {
            deleteElemOptim(preNode);
            insertElemAtBegin(data);
        } else {
            if (length >= this.capcacity) {
                deleteElemAtEnd();
            }
            insertElemAtBegin(data);
        }
    }

    /**
     * ��ӡ��������
     */
    private void printAll() {
        SNode node = headNode.getNext();
        while (node != null) {
            System.out.println(node.getElement() + ",");
            node = node.getNext();
        }
        System.out.println();
    }

    /**
     * ɾ��β���
     */
    private void deleteElemAtEnd() {
        SNode ptr = headNode;
//        ������ֱ�ӷ���
        if (ptr.getNext() == null) {
            return;
        }
        //�����ڶ����ڵ�
        while (ptr.getNext().getNext() != null) {
            ptr = ptr.getNext();
        }

        SNode tmp = ptr.getNext();
        ptr.setNext(null);
        tmp = null;
        length--;
    }

    /**
     * ��ȡ���ҵ�Ԫ�ص�ǰһ�����
     *
     * @param data
     * @return
     */
    private SNode findPreNode(T data) {
        SNode node = headNode;
        while (node.getNext() != null) {
            if (data.equals(node.getNext().getElement())) {
                return node;
            }
            node = node.getNext();
        }
        return null;
    }


    /**
     * ɾ��preNode�ڵ����һ��Ԫ��
     *
     * @param preNode
     */
    private void deleteElemOptim(SNode preNode) {
        SNode temp = preNode.getNext();
        preNode.setNext(temp.getNext());
        temp = null;
        length--;
    }

    /**
     * ����ͷ������ڵ�
     */
    public void insertElemAtBegin(T data) {
        SNode next = headNode.getNext();
        headNode.setNext(new SNode(data, next));
        length++;
    }

    public class SNode<T> {
        private T element;

        private SNode next;

        public SNode(T element) {
            this.element = element;
        }

        public SNode(T element, SNode next) {
            this.element = element;
            this.next = next;
        }

        public SNode() {
            this.next = null;
        }

        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public SNode getNext() {
            return next;
        }

        public void setNext(SNode next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        LRUBaseLinkedList list = new LRUBaseLinkedList();
        Scanner sc = new Scanner(System.in);
        while (true) {
            list.add(sc.nextInt());
            list.printAll();
        }
    }

}
