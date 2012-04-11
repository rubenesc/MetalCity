/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.stuff.sort;

/**
 *
 * @author Ruben
 */
import java.util.Comparator;
import java.util.TreeMap;

class Mycompare implements Comparator {

    public int compare(Object o1, Object o2) {
        Integer i1 = (Integer) o1;
        Integer i2 = (Integer) o2;
        return -i1.compareTo(i2);
    }
}
