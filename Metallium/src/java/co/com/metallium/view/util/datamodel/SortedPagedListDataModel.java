package co.com.metallium.view.util.datamodel;

import co.com.metallium.view.util.datamodel.PagedListDataModel;

/**
 * DataModel que permite paginar sobre un conjunto de datos, ultilizando el
 * concepto de "Lazy-Loading". Útil generalmente cuando los datos deben ser
 * obtenidos de una base de datos, pero no se desea almacenar todos los registros
 * en memoria.
 *
 * Además, guarda la información pertinente al ordenamiento de los datos,
 * indicando la columna por la cual se debe ordenar, y de qué forma se debe
 * ordenar (ascendente o descendentemente).
 *
 * @author Ruben
 */
public abstract class SortedPagedListDataModel extends PagedListDataModel {

    // Encabezados de ordenamiento
    protected String sortColumnName;
    protected boolean sortAscending;


    /**
     * Crea un {@code DataModel} que pagina a través de un conjunto de datos,
     * obteniendo el número especificado de filas en cada página.
     *
     * @param pageSize Cantidad de filas de la página
     */
    public SortedPagedListDataModel(int pageSize) {
        super(pageSize);
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
        if(!sortColumnName.equals(this.sortColumnName)){
            this.setDirtyData();
            this.sortColumnName = sortColumnName;
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
        if(sortAscending != (this.sortAscending)){
            this.setDirtyData();
            this.sortAscending = sortAscending;
        }
    }
}
