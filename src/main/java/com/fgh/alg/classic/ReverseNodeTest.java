package com.fgh.alg.classic;

import org.junit.jupiter.api.Test;
import com.fgh.alg.classic.ReverseNode.Node;

/**
 * @author fgh
 * @since 2019/4/17 15:35
 */
public class ReverseNodeTest {
    @Test
    public void reverseNode1() throws Exception {
        ReverseNode.Node<String> node4 = new ReverseNode.Node<>("4", null);
        ReverseNode.Node<String> node3 = new ReverseNode.Node<>("3", node4);
        ReverseNode.Node<String> node2 = new ReverseNode.Node<>("2", node3);
        ReverseNode.Node<String> node1 = new ReverseNode.Node("1", node2);

        ReverseNode reverseNode = new ReverseNode();
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode12() throws Exception {
        ReverseNode.Node<String> node1 = new ReverseNode.Node("1", null);

        ReverseNode reverseNode = new ReverseNode();
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode13() throws Exception {
        ReverseNode.Node<String> node1 = null;

        ReverseNode reverseNode = new ReverseNode();
        reverseNode.reverseNode1(node1);
    }

    /**
     * 头插法
     *
     * @throws Exception
     */
    @Test
    public void reverseHead21() throws Exception {
        ReverseNode.Node<String> node4 = new ReverseNode.Node<>("4", null);
        Node<String> node3 = new Node<>("3", node4);
        Node<String> node2 = new Node<>("2", node3);
        Node<String> node1 = new Node("1", node2);

        ReverseNode reverseNode = new ReverseNode();
        reverseNode.headInsertReverse(node1);
    }

    @Test
    public void recNodeTest31() {
        Node<String> node4 = new Node<>("4", null);
        Node<String> node3 = new Node<>("3", node4);
        Node<String> node2 = new Node<>("2", node3);
        Node<String> node1 = new Node("1", node2);

        ReverseNode reverseNode = new ReverseNode();
        reverseNode.recNode(node1);
    }

}
