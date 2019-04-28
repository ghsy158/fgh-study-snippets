package com.fgh.alg.linkedlist;

/**
 * @author fgh
 * @since 2019/4/12 11:30
 */
public class LinkedListAlgo {

    public static void main(String[] args) {
        Node node3 = new Node(3, null);
        Node node2 = new Node(2, node3);
        Node node1 = new Node(1, node2);

        Node reverseNode = reverse(node1);
        System.out.println(reverseNode);
    }

    /**
     * 反转列表
     *
     * @param list
     * @return
     */
    public static Node reverse(Node list) {
        Node curr = list, pre = null;
        while (curr != null) {
            Node next = curr.next;//2
            curr.next = pre;//null
            pre = curr;
            curr = next;
        }
        return pre;
    }


    /**
     * 检测环
     *
     * @param list
     * @return
     */
    public static boolean checkCircle(Node list) {
        if (list == null) return false;
        Node fast = list.next;
        Node slow = list;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) return true;
        }
        return false;
    }


    public static Node mergeSortedLists(Node la, Node lb) {
        if (la == null) return lb;
        if (lb == null) return la;
        Node p = la;
        Node q = lb;
        Node head;
        if (p.data < q.data) {
            head = p;
            p = p.next;
        } else {

        }
        return null;
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
