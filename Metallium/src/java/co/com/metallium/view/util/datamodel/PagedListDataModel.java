/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util.datamodel;

import javax.faces.model.DataModel;

/**
 * DataModel que permite paginar sobre un conjunto de datos, ultilizando el
 * concepto de "Lazy-Loading". Útil generalmente cuando los datos deben ser
 * obtenidos de una base de datos, pero no se desea almacenar todos los registros
 * en memoria. 
 *
 * @author Ruben
 */
public abstract class PagedListDataModel extends DataModel {

    // Tamaño de la página
    protected int pageSize;
    // Índice actual de la fila
    protected int rowIndex;
    // Página de Datos
    protected DataPage page;
    // Bandera que ejecuta la obtención de datos.
    protected boolean dirtyData = false;

    /**
     * Crea un {@code DataModel} que pagina a través de un conjunto de datos,
     * obteniendo el número especificado de filas en cada página.
     *
     * @param pageSize Cantidad de filas de la página
     */
    public PagedListDataModel(int pageSize) {
        super();
        this.pageSize = pageSize;
        this.rowIndex = -1;
        this.page = null;
    }

    @Override
    public void setWrappedData(Object o) {
        throw new UnsupportedOperationException("Los datos son cargados" +
                " por callback a través del método fetchData," +
                " en lugar de asignar explícitamente una lista.");
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public void setRowIndex(int index) {
        rowIndex = index;
    }

    @Override
    public int getRowCount() {
        // Se retorna el número total de datos en el
        // conjunto, nó solo los de la página
        return getPage().getDatasetSize();
    }

    /**
     * Retorna una página de datos. Si no existe ninguna, entonces se carga.
     * Esto no garantiza que la página retornada incluya en índice actual (ver
     * {@code getRowData()}
     */
    private DataPage getPage() {
        if (page != null){
            return page;
        }

        int startRow = rowIndex;
        if (rowIndex == -1) {
            // Aunque no haya una fila, se necesita obtener
            // una página de todos modos para conocer la cantidad de datos.
           startRow = 0;
        }

        page = fetchPage(startRow, pageSize);
        return page;
    }

    /**
     * Retorna el objeto correspondiente al índice actual. Si el objeto
     * almacenado en la página actual no incluye el índice, la página es
     * marcada como sucia ({@code dirty}), obteniendo la página apropiada
     * a través del método {@code fetchPage}.
     */
    @Override
    public Object getRowData(){
        if (rowIndex < 0) {
            throw new IllegalArgumentException(
                "El índice es inválido; no se encuentra en ninguna página. Valor: "+rowIndex);
        }

        // Aseguramos que la página exista.
        if (page == null) {
            page = fetchPage(rowIndex, pageSize);
            dirtyData = false;
        }

        // Evaluamos si el índice es igual a la fila inicial. Útil en
        // el ordenamiento de páginas.
        if (rowIndex == page.getStartRow() && dirtyData){
            page = fetchPage(rowIndex, pageSize);
            dirtyData = false;
        }

        int datasetSize = page.getDatasetSize();
        int startRow = page.getStartRow();
        int nRows = page.getData().size();
        int endRow = startRow + nRows;

        if (rowIndex >= datasetSize) {
            throw new IllegalArgumentException(
                    "El índice es inválido; no se encuentra en ninguna página. Valor: "+rowIndex);
        }

        if (rowIndex < startRow) {
            page = fetchPage(rowIndex, pageSize);
            startRow = page.getStartRow();
        } else if (rowIndex >= endRow) {
            page = fetchPage(rowIndex, pageSize);
            startRow = page.getStartRow();
        }

        return page.getData().get(rowIndex - startRow);
    }

    @Override
    public Object getWrappedData() {
        return page.getData();
    }

    /**
     * Retorna verdadero en caso que el índice exista en el conjunto de datos.
     * Puede que la página contenga o no el elemento, en este caso el método
     * {@code getRowData} obtendrá la nueva página.
     */
    @Override
    public boolean isRowAvailable() {
        DataPage currentPage = getPage();
        if (currentPage == null) {
            return false;
        }

        if (rowIndex < 0) {
            return false;
        } else if (rowIndex >= currentPage.getDatasetSize()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Método que obtiene la página requerida del conjunto de datos objetivo.
     *
     * @param startRow Fila desde la que se debe obtener la página.
     * @param pageSize Tamaño total de objetos en la página.
     */
    public abstract DataPage fetchPage(int startRow, int pageSize);

    /**
     * Marca si los datos como sucios, la página debe ser obtenida nuevamente.
     * @param dirtyData
     */
    public boolean isDirtyData() {
        return dirtyData;
    }

    /**
     * Método que especifica si los datos deben ser obtenidos nuevamente.
     * @return Verdadero si los datos deben ser obtenidos nuevamente.
     */
    public void setDirtyData(boolean dirtyData) {
        this.dirtyData = dirtyData;
    }

    /**
     * Marca si los datos de la página deben ser obtenidos nuevamente.
     * @param dirtyData
     */
    public void setDirtyData() {
        dirtyData = true;
    }

    /**
     * Indica el tamaño de la página.
     * @return El tamaño de la página
     */
    public int getPageSize() {
        return pageSize;
    }
}
