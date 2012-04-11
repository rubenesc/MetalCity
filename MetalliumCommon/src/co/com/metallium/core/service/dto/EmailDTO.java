/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto;

import java.io.Serializable;

/**
 * 20101214
 * @author Ruben
 */
public class EmailDTO implements Serializable {

    private String[] emailAddress;
    private String body;
    private String subject;

    public EmailDTO() {
    }

    public EmailDTO(String[] emailAddress, String body, String subject) {
        this.emailAddress = emailAddress;
        this.body = body;
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String[] emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "EnviarEmailDTO{" + "destino=" + emailAddress + "body=" + body + "subject=" + subject + '}';
    }



}
