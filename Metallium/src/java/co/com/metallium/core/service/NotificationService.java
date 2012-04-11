/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.entity.Notification;
import co.com.metallium.core.service.dto.search.NotificationSearchDTO;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20110112
 * @author Ruben
 */
@Stateless
public class NotificationService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private MessageService messageService;

    public NotificationService() {
    }

    public List<Notification> findQueryByRange(NotificationSearchDTO criteriaDTO) {

        List<Notification> answer = null;

        try {
            Query q = buildQuery(criteriaDTO, false);
            answer = q.getResultList();
        } catch (Exception e) {
            answer = new ArrayList<Notification>();
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    public int countQuery(NotificationSearchDTO criteriaDTO) {
        int answer = 0;
        try {
            Query q = buildQuery(criteriaDTO, true);
            answer = ((Number) q.getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    private Query buildQuery(NotificationSearchDTO criteriaDTO, boolean isItToCount) {

        Query q = null;

        String searchQuery = null;

        if (isItToCount) {
            searchQuery = "Notification.countNotificationsByUserTo";
        } else {
            searchQuery = "Notification.findNotificationsByUserTo";
        }

        try {
            q = em.createNamedQuery(searchQuery);
            q.setParameter("userTo", criteriaDTO.getIdUserTo());
            q.setParameter("date", MetUtilHelper.getNotificationsSinceThisDay());

            int[] range = criteriaDTO.getRange();
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);

        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return q;
    }

    /**
     * Inform the user that there is a new event so he should look and see
     *
     * @param idUser
     */
    @Asynchronous
    public void userNotificationNew(int idUser) {
        updateUserNotification(idUser, true);
    }

    @Asynchronous
    public void userMessageNew(int idUser) {
        updateUserNewMessage(idUser, true);
    }


    /**
     * Just turn off the notification sign. This is because the user already read the notification we
     * wanted him to see.
     *
     *
     * @param idUser
     */
    @Asynchronous
    public void userNotificationRead(int idUser) {

        if (MetConfiguration.XXX) {
            //turn off notificacions when goes just to the inbox.
            updateUserNotification(idUser, false);
        } else {
            //turn off notificacions when he has no unread messages.
            if (messageService.countUserInboxUnreadMessages(idUser) == 0) {
                updateUserNotification(idUser, false);
            }
        }

    }

    @Asynchronous
    public void userNewMessageRead(int idUser) {

        updateUserNewMessage(idUser, false);

    }

    /**
     * This method is to inform a User that he should look at his Notification Box
     * because there is some activity there. So we update the parameter "notifications"
     * on his User register so that in some time he can be aware of it and just go
     * and check the notifications box.
     *
     * @param idUser
     * @param notificationState = true or false. True = go check the notification box. False = no new notifications.
     *
     */
    private void updateUserNotification(int idUser, boolean notificationState) {

        try {

            Query q = em.createNamedQuery("User.updateUserNotifications");
            q.setParameter("notifications", notificationState);
            q.setParameter("id", idUser);

            if (q.executeUpdate() == 0) {
                LogHelper.makeLog("Error ... updateUserNotification failed, executeUpdate updated 0 entities for user id: " + idUser);
            }

        } catch (Exception e) {
            //I cant do Shit
            LogHelper.makeLog("I couldnt update the user notification: " + e.getMessage(), e);
        }

    }

    private void updateUserNewMessage(int idUser, boolean newMessageState) {

        try {

            Query q = em.createNamedQuery("User.updateUserNewMessage");
            q.setParameter("newMessage", newMessageState);
            q.setParameter("id", idUser);

            if (q.executeUpdate() == 0) {
                LogHelper.makeLog("Error ... updateUserNewMessage failed, executeUpdate updated 0 entities for user id: " + idUser);
            }

        } catch (Exception e) {
            //I cant do Shit
            LogHelper.makeLog("I couldnt update the user newMessage: " + e.getMessage(), e);
        }

    }
}
