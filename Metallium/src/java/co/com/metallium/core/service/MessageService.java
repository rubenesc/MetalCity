/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MessageConst;
import co.com.metallium.core.entity.Message;
import co.com.metallium.core.exception.MessageException;
import co.com.metallium.core.exception.UserException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.service.dto.search.MessageSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20101231
 * @author Ruben
 */
@Stateless
public class MessageService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    NotificationService notificationService;
    @EJB
    private ApplicationParameters applicationParameters;

    public MessageService() {
    }

    /**
     * What this method does is that it changes the Message read state
     * from unread to read, so that when it is displayed its not highlighted 
     * like an unread message.
     * 
     */
    public void readMessageById(Message message) {
        try {
            if (!MetUtilHelper.isMessageRead(message.getMessageRead())) {
                //If message is NOT read then I change its state to read.
                message.setMessageRead(MessageConst.read);
                em.merge(message);
            }

        } catch (Exception e) {
            //I cant do anything.
            LogHelper.makeLog(e);
        }

    }

    public Message findMessageById(int id) throws MessageException {
        Message entity = null;

        try {
            entity = (Message) em.createNamedQuery("Message.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            if (ex instanceof NoResultException) {
                String msg1 = "The Message with id: " + id + ", could not be found in the system.";
                String msg2 = applicationParameters.getResourceBundleMessage("common_info_no_result_exception");
                throw new MessageException(msg1, ex, msg2);
            } else {
                throw new MessageException(ex.getMessage(), ex);
            }
        }

        return entity;
    }

    /**
     *
     * @param id = Message Id
     * @param isDeleteSender = tells me if the one deleting the message is the render or the recipient
     * because when both of them have marked the message as deleted then I can remove the message from
     * the DB.
     *
     * @throws MessageException
     */
    public void deleteMessage(Integer id, boolean isDeleteSender) throws MessageException {

        Message message = this.findMessageById(id);

        try {
            boolean removeMessageFromDB = false;

            if (isDeleteSender) {
                message.setDeleteSender(MessageConst.delete_yes);
            } else {
                message.setDeleteRecipient(MessageConst.delete_yes);
            }

            //I check if Im gonna delete the fucking message from the DB or not.
            if (message.getDeleteSender().compareTo(MessageConst.delete_yes) == 0
                    && message.getDeleteRecipient().compareTo(MessageConst.delete_yes) == 0) {
                //Yes Im gonna delete the bastard.
                removeMessageFromDB = true;

            }

            if (removeMessageFromDB) {
                em.remove(message);
            } else {
                em.merge(message);
            }
            em.flush();
        } catch (Exception ex) {
            throw new MessageException(ex.getMessage(), ex);
        }

    }

    public void sendMessage(Message newMessage) throws MessageException, UserException, ValidationException {

        newMessage.setMessage(HtmlSanitizer.admin(newMessage.getMessage())); //Clean HTML input.
        validateWallPostLength(newMessage.getMessage());

        try {
            //The message title is just a preview of the content.
            String stringPreview = DataHelper.getMessagePreviewString(newMessage.getMessage());
            newMessage.setTitle(stringPreview);

            newMessage.setDate(new Date());
            newMessage.setMessageRead(MessageConst.unread);
            newMessage.setDeleteSender(MessageConst.delete_no); //This means that the messege is not deleted.
            newMessage.setDeleteRecipient(MessageConst.delete_no); //This means that the messege is not deleted.

            em.persist(newMessage);
            em.flush();
        } catch (Exception e) {
            throw new MessageException(e.getMessage(), e);
        }

        //I notify the "idUser2" that he has a new Message
        this.notificationService.userMessageNew(newMessage.getToUser().getId());


    }

    /*
     * Counts the number of messages a user has in his inbox. It does not distinguish
     * between read and unread messages.
     *
     * the parameter deleteRecipent indicates that the message is not delete, in other
     * words, that it is still available in the users inbox.
     *
     */
    public int countUserBoxMessages(MessageSearchDTO criteriaDTO) {
        int answer = 0;
        try {

            if (MessageConst.isCanI(criteriaDTO.getBox(), MessageConst.inbox)) {
                answer = ((Number) em.createNamedQuery("Message.countUserInboxMessages").setParameter("toUserId", criteriaDTO.getToUser()).setParameter("deleteRecipient", MessageConst.delete_no).getSingleResult()).intValue();
            } else if (MessageConst.isCanI(criteriaDTO.getBox(), MessageConst.sent)) {
                answer = ((Number) em.createNamedQuery("Message.countUserSentMessages").setParameter("fromUserId", criteriaDTO.getToUser()).setParameter("deleteSender", MessageConst.delete_no).getSingleResult()).intValue();
            }

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    /**
     *
     * @param idUser
     * @return number of user unread messages
     */
    public int countUserInboxUnreadMessages(int idUser) {
        int answer = 0;
        try {

            answer = ((Number) em.createNamedQuery("Message.countUserInboxUnreadMessages").setParameter("toUserId", idUser).setParameter("deleteRecipient", MessageConst.delete_no).setParameter("messageRead", MessageConst.unread).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    public List<Message> findUserBoxMessagesByRange(MessageSearchDTO criteriaDTO) {

        List<Message> answer = null;

        try {

            Query q = null;
            boolean boxSelect = false;
            if (MessageConst.isCanI(criteriaDTO.getBox(), MessageConst.inbox)) {
                q = em.createNamedQuery("Message.findUserInboxMessages");
                q.setParameter("toUserId", criteriaDTO.getToUser());
                q.setParameter("deleteRecipient", MessageConst.delete_no);
                boxSelect = true;
            } else if (MessageConst.isCanI(criteriaDTO.getBox(), MessageConst.sent)) {
                q = em.createNamedQuery("Message.findUserSentMessages");
                q.setParameter("fromUserId", criteriaDTO.getToUser());
                q.setParameter("deleteSender", MessageConst.delete_no);
                boxSelect = true;
            }

            if (boxSelect) {
                int[] range = criteriaDTO.getRange();
                q.setMaxResults(range[1] - range[0]);
                q.setFirstResult(range[0]);
                answer = q.getResultList();
            } else {
                answer = new ArrayList<Message>();
            }

        } catch (Exception e) {
            answer = new ArrayList<Message>();
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    public void messageNotificationRead(int idUser) {
        this.notificationService.userNotificationRead(idUser);
    }

    public void userNewMessageRead(int idUser) {
        this.notificationService.userNewMessageRead(idUser);
    }
}
