/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff.btree;

/**
 *
 * @author Ruben
 */
public class Node {
    
    private int value;
    private String message;
    private Node left;
    private Node right;
    
    public Node() {
    }

    public Node(String message,int value, Node left, Node right) {
        this.message = message;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
    
    
    
    
}
