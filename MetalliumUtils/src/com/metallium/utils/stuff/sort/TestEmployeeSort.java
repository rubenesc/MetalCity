/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.stuff.sort;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ruben
 */
public class TestEmployeeSort {

    private static EmpSortByName sortByName = new EmpSortByName();

    public static void main(String[] args) {
        testList();
    }

    private static void testMap() {

    }


    private static void testList() {
        List coll = Util.getEmployeesList();
//        Collections.sort(coll); // sort method
        Collections.sort(coll, sortByName);
        printList(coll);

    }

    private static void printList(List<Employee> list) {
        System.out.println("EmpId\tName\tAge");
        for (Employee e : list) {
            System.out.println(e.getEmpId() + "\t" + e.getName() + "\t" + e.getAge());
        }
    }
}
