/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.dto;

import java.io.Serializable;

/**
 * THis is just a rare helper DTO
 * when I want to return just 2 Strings in an answer.
 * Sounds Lame, but I know I this can simplify some stuff.
 * 
 * It was supposed to be just for 2 values, now I added a third one jajajaja 
 *
 * @author Ruben
 */
public class PairDTO implements Serializable {

    private String one;
    private Integer codeInt;
    private String two;
    private String three;

    public PairDTO() {
    }

    public PairDTO(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public PairDTO(Integer codeInt, String two) {
        this.codeInt = codeInt;
        this.two = two;
    }

    public PairDTO(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }
    
    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public Integer getCodeInt() {
        return codeInt;
    }

    public void setCodeInt(Integer codeInt) {
        this.codeInt = codeInt;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }
    
    @Override
    public String toString() {
        return "PairDTO{" + "one=" + one + ", two=" + two + '}';
    }

    

}
