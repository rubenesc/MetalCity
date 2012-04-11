/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.stuff.sort;

import java.util.TreeMap;

/**
 *
 * @author Ruben
 */
public class TreeMapCom {

    public static void main(String[] args) {
        TreeMap map = new TreeMap(new Mycompare());
        map.put(new Integer(100), "orange");
        map.put(new Integer(300), "apple");
        map.put(new Integer(200), "grapes");
        map.put(new Integer(400), "grapes");
        System.out.println("The TreeeMap Is " + map);
//    Try to place the null you will get NullPointerException
        //map.put(null,"javasun");
    }
}
