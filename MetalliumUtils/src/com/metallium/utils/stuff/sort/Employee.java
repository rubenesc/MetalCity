/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.stuff.sort;

/**
 *
 * @author Ruben
 */
public class Employee  implements Comparable<Employee> {

    private int empId;
    private String name;
    private int age;

    public Employee(int empId, String name, int age) {
        this.empId = empId;
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Employee o){
//        return this.empId - o.getEmpId();
        return o.age  - this.age;
    }
}
