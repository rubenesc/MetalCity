/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff.btree;

import java.util.Stack;

/**
 *
 * @author Ruben
 */
public class BTest {

    public static void main(String[] args) {

        Node tree = createTree1();
        findNodeTest(tree);
        findMaxMinNodeTest(tree);
        System.out.println("----------------------");

        tree = createTree2();
        tranverseTest(tree);
        System.out.println("----------------------");

        tree = createTree3();
        findLowestCommonAncestorTest(tree);
        System.out.println("----------------------");

        tree = createTree1();
        findFatherTest(tree);
        System.out.println("----------------------");

        printTree(tree);
        isBstTest(tree);

    }

    private static void isBstTest(Node node) {
        System.out.println("*** isBst Test ***");
        if (isBinarySearchTree(node, Integer.MIN_VALUE, Integer.MAX_VALUE)){
            System.out.println("It is a BST");
        } else {
            System.out.println("It is NOT a BST");
        }
        
    }

    public static boolean isBinarySearchTree(Node node, Integer min, Integer max) {
        if (node.getValue() < min || node.getValue() > max) {
            return false;
        }
        //Check this node!
        //This algorithm doesn't recurse with null Arguments.
        //When a null is found the method returns true;
        //Look and you will find out.
    /*
         * Checking for Left SubTree
         */
        boolean leftIsBst = false;
        //If the Left Node Exists
        if (node.getLeft() != null) {
            //and the Left Data are Smaller than the Node Data
            if (node.getLeft().getValue() < node.getValue()) {
                //Check if the subtree is Valid as well
                leftIsBst = isBinarySearchTree(node.getLeft(), min, node.getValue());
            } else {
                //Else if the Left data are Bigger return false;
                leftIsBst = false;
            }
        } else //if the Left Node Doesn't Exist return true;
        {
            leftIsBst = true;
        }

        /*
         * Checking for Right SubTree - Similar Logic
         */
        boolean rightIsBst = false;
        //If the Right Node Exists
        if (node.getRight() != null) {
            //and the Right Data are Bigger (or Equal) than the Node Data
            if (node.getRight().getValue() >= node.getValue()) {
                //Check if the subtree is Valid as well
                rightIsBst = isBinarySearchTree(node.getRight(), node.getValue() + 1, max);
            } else {
                //Else if the Right data are Smaller return false;
                rightIsBst = false;
            }
        } else //if the Right Node Doesn't Exist return true;
        {
            rightIsBst = true;
        }

        //if both are true then this means that subtrees are BST too
        return (leftIsBst && rightIsBst);
    }

    private static void findMaxMinNodeTest(Node nRoot) {
        System.out.println("*** findMaxNodeTest Test ***");
        Node aux = findMaxNode(nRoot);
        if (aux != null) {
            System.out.println("Max Node Value --> " + aux.getMessage());
        } else {
            System.out.println("Sorry we couldn't find max node");
        }

        aux = findMinNode(nRoot);
        if (aux != null) {
            System.out.println("Min Node Value --> " + aux.getMessage());
        } else {
            System.out.println("Sorry we couldn't find min node");
        }

        aux = findMinNode2(nRoot);
        if (aux != null) {
            System.out.println("Min2 Node Value --> " + aux.getMessage());
        } else {
            System.out.println("Sorry we couldn't find min2 node");
        }



    }

    private static void findNodeTest(Node nRoot) {
        System.out.println("*** findNode Test ***");
        int nodeToFind = 7;
        //Lookup is an O(log(n)) operation in a binary search tree.
        Node aux = findNode(nRoot, nodeToFind);
        if (aux != null) {
            System.out.println("value --> " + aux.getMessage());
        } else {
            System.out.println("Sorry we couldn't find node# " + nodeToFind);
        }
    }

    private static void tranverseTest(Node nRoot) {
        System.out.println("*** Tranverse Test ***");
        //Every node is examined once, so it‘s O(n).
        preorderTransversal(nRoot);
    }

    private static void findFatherTest(Node tree) {
        System.out.println("*** findFatherTest Test ***");
        int v = 4;

        //this is an O(log(n)) algorithm.
        Node aux = findFather(tree, v);
        if (aux != null) {
            System.out.println("... the father of " + v + " --is--> " + aux.getValue());
        } else {
            System.out.println("Sorry we couldn't find the father");
        }

        System.out.println("");
        v = 12;

        //this is an O(log(n)) algorithm.
        aux = findFather(tree, v);
        if (aux != null) {
            System.out.println("... the father of " + v + " --is--> " + aux.getValue());
        } else {
            System.out.println("Sorry we couldn't find the father");
        }




    }

    private static void findLowestCommonAncestorTest(Node tree) {
        System.out.println("*** findLowestCommonAncestor Test ***");
        int v1 = 4;
        int v2 = 12;

        //this is an O(log(n)) algorithm.
        Node aux = findLowestCommonAncestor(tree, v1, v2);
        System.out.println("... ancestor of " + v1 + " - " + v2);
        if (aux != null) {
            System.out.println("Ancestor --> " + aux.getMessage());
        } else {
            System.out.println("Sorry we couldn't find the ancestor");
        }
    }

    /**
     * this is an O(log(n)) algorithm.
     * 
     */
    private static Node findLowestCommonAncestor(Node node, int v1, int v2) {

        while (node != null) {

            int nodeValue = node.getValue();

            if (nodeValue < v1 && nodeValue < v2) {
                node = node.getRight();
            } else if (nodeValue > v1 && nodeValue > v2) {
                node = node.getLeft();
            } else {
                return node;
            }
        }
        return null;
    }

    private static Node findFather(Node node, int son) {

        if (node == null) {
            return node;
        }

        int nodeValue = node.getValue();
        if (nodeValue < son) {
            if (node.getRight() != null && node.getRight().getValue() == son) {
                return node;
            } else {
                return findFather(node.getRight(), son);
            }
        } else {
            if (node.getLeft() != null && node.getLeft().getValue() == son) {
                return node;
            } else {
                return findFather(node.getLeft(), son);
            }
        }

    }

    /**
     * Every node is examined once, so it‘s O(n).
     * 
     * @param root 
     */
    private static void preorderTransversal(Node root) {

        if (root == null) {
            return;
        }
        System.out.println(root.getValue());
        preorderTransversal(root.getLeft());
        preorderTransversal(root.getRight());
    }

    /**
     * Lookup is an O(log(n)) operation in a binary search tree.
     * 
     * @param root
     * @param value
     * @return 
     */
    public static Node findNode(Node root, int value) {

        if (root != null) {

            if (root.getValue() == value) {
                return root;
            }

            if (root.getValue() < value) {
                return findNode(root.getRight(), value);
            } else {
                return findNode(root.getLeft(), value);
            }

        }

        return root;
    }

    public static Node findMaxNode(Node node) {
        if (node.getRight() == null) {
            return node;
        } else {
            return findMaxNode(node.getRight());
        }
    }

    public static Node findMinNode(Node node) {
        if (node.getLeft() == null) {
            return node;
        } else {
            return findMinNode(node.getLeft());
        }
    }

    public static Node findMinNode2(Node node) {
        if (node == null || (node.getLeft() == null && node.getRight() == null)) {
            return node;
        } else {

            if (node.getLeft() == null) {
                return findMinNode(node.getRight());
            } else if (node.getRight() == null) {
                return findMinNode(node.getLeft());
            }

            int v1 = node.getLeft().getValue();
            int v2 = node.getRight().getValue();

            if (v1 < v2) {
                return findMinNode(node.getLeft());
            } else {
                return findMinNode(node.getRight());
            }

        }
    }

    private static Node createTree1() {
        Node n11 = new Node("Node #11", 11, null, null);
        Node n13 = new Node("Node #13", 13, null, null);
        Node n1 = new Node("Node #1", 1, null, null);
        Node n2 = new Node("Node #4", 4, null, null);
        Node n3 = new Node("Node #7", 7, null, null);
        Node n4 = new Node("Node #12", 12, n11, n13);
        Node n5 = new Node("Node #3", 3, n1, n2);
        Node n6 = new Node("Node #10", 10, n3, n4);
        Node nRoot = new Node("Root Node!!!", 5, n5, n6);
        return nRoot;
    }

    private static Node createTree2() {
        Node n1 = new Node("Node #25", 25, null, null);
        Node n2 = new Node("Node #75", 75, null, null);
        Node n3a = new Node("Node #110", 110, null, null);
        Node n3 = new Node("Node #125", 125, n3a, null);
        Node n4 = new Node("Node #175", 175, null, null);
        Node n5 = new Node("Node #3", 50, n1, n2);
        Node n6 = new Node("Node #10", 150, n3, n4);
        Node nRoot = new Node("Root Node!!!", 100, n5, n6);
        return nRoot;
    }

    private static Node createTree3() {
        Node n1 = new Node("Node #10", 10, null, null);
        Node n2 = new Node("Node #14", 14, null, null);

        Node n4 = new Node("Node #12", 12, n1, n2);

        Node n5 = new Node("Node #4", 4, null, null);
        Node n8 = new Node("Node #8", 8, n5, n4);


        Node n6 = new Node("Node #22", 22, null, null);
        Node nRoot = new Node("Root Node!!!", 20, n8, n6);
        return nRoot;
    }
    //************************************************************************//
    //************************************************************************//

    private static void printTree(Node node) {
        System.out.println("*** printTree ***");
        if (node == null) {
            return;
        }
        int h = treeHeight(node);
        System.out.println("height: " + h);

        System.out.println("--> print tree level by level");
        for (int i = 1; i <= h; i++) {
            System.out.print("level " + i + ": ");
            printTreeLevel(node, i);
            System.out.println("");
        }


        System.out.println("");
        System.out.println("*** max path ***");

///*Function to print level order traversal of tree*/
//printLevelorder(tree)
//for d = 1 to height(tree)
//   printGivenLevel(tree, d);
//
///*Function to print all nodes at a given level*/
//printGivenLevel(tree, level)
//if tree is NULL then return;
//if level is 1, then
//    print(tree->data);
//else if level greater than 1, then
//    printGivenLevel(tree->left, level-1);
//    printGivenLevel(tree->right, level-1);        

    }

    private static void printTreeLevel(Node node, int level) {
        if (node == null) {
            return;
        }

        if (level == 1) {
            System.out.print(node.getValue() + ",");
        } else {
            printTreeLevel(node.getLeft(), level - 1);
            printTreeLevel(node.getRight(), level - 1);
        }


    }

    private static int treeHeight(Node node) {
        if (node == null) {
            return 0;
        }

        int l = treeHeight(node.getLeft());
        int r = treeHeight(node.getRight());

        return (r > l) ? r + 1 : l + 1;
    }
}
