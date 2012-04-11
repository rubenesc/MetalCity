/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.stuff.sort;

import java.util.Comparator;

/**
 *
 * @author Ruben
 */
public class EmpSortByName implements Comparator<Employee> {

    public int compare(Employee o1, Employee o2) {

        return o1.getName().compareTo(o2.getName());
        
    }

}
