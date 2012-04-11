/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.stuff.sort;

import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Ruben
 */
public class SortMap {

    public static void main(String[] args) {

        HashMap<String,Double> map = new HashMap<String,Double>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String,Double> sorted_map = new TreeMap(bvc);

        map.put("A",99.5);
        map.put("B",67.4);
        map.put("C",67.5);
        map.put("D",67.3);

        System.out.println("unsorted map");
        for (String key : map.keySet()) {
            System.out.println("key/value: " + key + "/"+map.get(key));
        }

        sorted_map.putAll(map);

        System.out.println("results");
        for (String key : sorted_map.keySet()) {
            System.out.println("key/value: " + key + "/"+sorted_map.get(key));
        }
    }


}
