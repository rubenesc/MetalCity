/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.service.dto;

import java.io.Serializable;

/**
 * 20110424
 * @author Ruben
 *
 * Waring this DTO is being used in a select statement in User entity, so
 * be careful if you modify this class in any way.
 *
 */
public class NotificationDTO implements Serializable {

    private Boolean notifications;
    private Boolean newMessage;

    public NotificationDTO() {
    }

    public NotificationDTO(Boolean notifications, Boolean newMessage) {
        this.notifications = notifications;
        this.newMessage = newMessage;
    }

    public Boolean getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Boolean newMessage) {
        this.newMessage = newMessage;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }





}
