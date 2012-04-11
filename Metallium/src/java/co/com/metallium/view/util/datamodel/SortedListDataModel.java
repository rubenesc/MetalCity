/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util.datamodel;

import java.util.List;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Ruben
 */
public abstract class SortedListDataModel extends ListDataModel {

    // Encabezados de ordenamiento
    protected String sortColumnName;
    protected boolean sortAscending;

    private boolean dirtyData = false;

    public SortedListDataModel() {
        super();
    }

    /**
     * Crea un {@code DataModel} que pagina a partir de una lista de datos.
     *
     * @param list La lista de objetos
     */
    public SortedListDataModel(List list) {
        super(list);
    }

    /**
     * Gets the sortColumnName column.
     *
     * @return column to sortColumnName
     */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /**
     * Sets the sortColumnName column.
     *
     * @param sortColumnName column to sortColumnName
     */
    public void setSortColumnName(String sortColumnName) {
        if(!sortColumnName.equals(this.sortColumnName)) {
            this.sortColumnName = sortColumnName;
            this.dirtyData = true;
        }
    }

    /**
     * Is the sortColumnName sortAscending?
     *
     * @return true if the sortAscending sortColumnName otherwise false
     */
    public boolean isSortAscending() {
        return sortAscending;
    }

    /**
     * Sets sortColumnName type.
     *
     * @param sortAscending true for sortAscending sortColumnName, false for descending sortColumnName.
     */
    public void setSortAscending(boolean sortAscending) {
        if(sortAscending != (this.sortAscending)) {
            this.sortAscending = sortAscending;
            this.dirtyData = true;
        }
    }

    @Override
    public Object getRowData() {
        if( dirtyData ) {
            this.dirtyData = false;
            this.setWrappedData( sort() );
        }
        return super.getRowData();
    }

    @Override
    public Object getWrappedData() {
        if( dirtyData ) {
            this.dirtyData = false;
            this.setWrappedData( sort() );
        }
        return super.getWrappedData();
    }

    /**
     * Método que se encarga de obtener ordenar la lista de datos. Se debe asegurar
     * la disponibilidad de las propiedades {@code sortAscending} y {@code sortColumnName}
     */
    public abstract List sort();
}
