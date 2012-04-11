/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.stuff.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Ruben
 */
public class Util {

    public static List<Employee> getEmployeesList() {

        List<Employee> col = new ArrayList<Employee>();

        col.add(new Employee(5, "Frank", 28));
        col.add(new Employee(1, "Jorge", 19));
        col.add(new Employee(6, "Bill", 34));
        col.add(new Employee(3, "Michel", 10));
        col.add(new Employee(7, "Simpson", 8));
        col.add(new Employee(4, "Clerk",16 ));
        col.add(new Employee(8, "Lee", 40));
        col.add(new Employee(2, "Mark", 30));

        return col;
    }


    public static Map<Integer, Employee> getEmployeesMap() {

        TreeMap<Integer, Employee> col = new TreeMap<Integer, Employee>();

        col.put(5, new Employee(5, "Frank", 28));

        col.put(1, new Employee(1, "Jorge", 19));
        col.put(6, new Employee(6, "Bill", 34));
        col.put(3, new Employee(3, "Michel", 10));
        col.put(7, new Employee(7, "Simpson", 8));
        col.put(4, new Employee(4, "Clerk",16 ));
        col.put(8, new Employee(8, "Lee", 40));
        col.put(2, new Employee(2, "Mark", 30));

        return col;
    }

}
