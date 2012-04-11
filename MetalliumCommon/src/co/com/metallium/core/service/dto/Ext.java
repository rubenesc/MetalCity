/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto;

import javax.persistence.Transient;

/**
 *
 * @author Ruben
 */
public class Ext {

    @Transient
    private Integer idUserAuthenticated;
    @Transient
    private String sessionId;
    @Transient
    private String accion;

    public Ext() {
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Integer getIdUserAuthenticated() {
        return idUserAuthenticated;
    }

    public void setIdUserAuthenticated(Integer idUserAuthenticated) {
        this.idUserAuthenticated = idUserAuthenticated;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Ext{" + "idUserAuthenticated=" + idUserAuthenticated + "sessionId=" + sessionId + "accion=" + accion + '}';
    }

}
