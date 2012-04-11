/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.util.table;

/**
 *
 * @author Ruben
 */
public class RowSelectableData<T> {
    
    private boolean selected;
    private T data;

    public RowSelectableData(T data) {
        this.data = data;
        this.selected = false;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
