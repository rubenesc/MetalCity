/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.search;

import java.io.Serializable;

/**
 * Clase Base de los criterios de realizacion de las busquedas
 * @author Ruben Escudero
 */
public class SearchBaseDTO implements Serializable {

    //My Stuff
    private int[] range;
    private String sqlCondition; //this is the "AND" or "OR" of the SQL Im going to execute. Its not important, its "AND" by default, and its only used on counted occasions.
    private Class theClass = null; //Class Type of the list<T> Im going to return the result.
    private String orderBy = null;

    protected SearchBaseDTO() {
    }

    public int[] getRange() {
        return range;
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public Class getTheClass() {
        return theClass;
    }

    public void setTheClass(Class theClass) {
        this.theClass = theClass;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
