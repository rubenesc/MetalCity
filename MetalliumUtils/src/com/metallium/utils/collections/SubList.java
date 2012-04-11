/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ruben
 */
public class SubList<T> implements Serializable {

    private List lista;
    private long size;

    public SubList() {
        setLista(new ArrayList<T>());
    }

    public SubList(List<T> lista, long size) {
        setLista(lista);
        setSize(size);
    }

    public List<T> getLista() {
        return lista;
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
