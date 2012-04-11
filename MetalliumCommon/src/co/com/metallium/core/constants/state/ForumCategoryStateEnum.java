/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.constants.state;

/**
 *
 * @author Ruben
 */
public enum ForumCategoryStateEnum {

    ACTIVE(Short.valueOf("1")),
    INACTIVE(Short.valueOf("2")),
    DELETED(Short.valueOf("3"));
    
    ForumCategoryStateEnum(Short state){
        this.state = state;
    }
    
    private Short state;

    public Short getState() {
        return state;
    }
    
}
