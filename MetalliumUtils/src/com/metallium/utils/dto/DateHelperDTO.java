/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.dto;

import java.io.Serializable;

/**
 * 20101113
 * @author Ruben
 */
public class DateHelperDTO implements Serializable{

    private String year = "";
    private String month = "";
    private String day = "";

    public DateHelperDTO() {
    }

    public DateHelperDTO(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
