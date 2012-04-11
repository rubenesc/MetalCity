/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.dto;

import java.io.Serializable;

/**
 * 20110519
 * @author Ruben
 */
public class SqlAddOnDTO implements Serializable {

    private String tables;
    private String parameters;

    public SqlAddOnDTO() {
    }

    public SqlAddOnDTO(String tables, String parameters) {
        this.tables = tables;
        this.parameters = parameters;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }




}
