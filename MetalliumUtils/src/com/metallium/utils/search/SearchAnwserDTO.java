/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.search;

import com.metallium.utils.collections.SubList;
import java.io.Serializable;

/**
 * DTO generico de respuesta para la paginacion de tablas
 * @author Ruben
 */
public class SearchAnwserDTO <T extends Object>  implements Serializable {

    private SubList<T> listaResultados;
    private int registroActual;

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public SearchAnwserDTO() {
    }

    public SubList<T> getListaResultados() {
        return listaResultados;
    }

    public void setListaResultados(SubList<T> listaResultados) {
        this.listaResultados = listaResultados;
    }



}

