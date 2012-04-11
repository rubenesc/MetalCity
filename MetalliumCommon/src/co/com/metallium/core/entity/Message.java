/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.entity;

import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 20101230
 * @author Ruben
 */
@Entity
@Table(name = "met_message")
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByDate", query = "SELECT m FROM Message m WHERE m.date = :date"),
    @NamedQuery(name = "Message.findUserInboxMessages", query = "Select m From Message m WHERE m.toUser.id = :toUserId and m.deleteRecipient = :deleteRecipient order by m.date desc"),
    @NamedQuery(name = "Message.countUserInboxMessages", query = "Select COUNT(DISTINCT m.id) From Message m WHERE m.toUser.id = :toUserId and m.deleteRecipient = :deleteRecipient "),
    @NamedQuery(name = "Message.countUserInboxUnreadMessages", query = "Select COUNT(DISTINCT m.id) From Message m WHERE m.toUser.id = :toUserId and m.deleteRecipient = :deleteRecipient and m.messageRead = :messageRead "),
    @NamedQuery(name = "Message.findUserSentMessages", query = "Select m From Message m WHERE m.fromUser.id = :fromUserId and m.deleteSender = :deleteSender order by m.date desc"),
    @NamedQuery(name = "Message.countUserSentMessages", query = "Select COUNT(DISTINCT m.id) From Message m WHERE m.fromUser.id = :fromUserId and m.deleteSender = :deleteSender"),
    @NamedQuery(name = "Message.findByTitle", query = "SELECT m FROM Message m WHERE m.title = :title")})

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @Column(name = "message_read")
    private Integer messageRead;
    @Basic(optional = false)
    @Column(name = "delete_sender")
    private Integer deleteSender;
    @Basic(optional = false)
    @Column(name = "delete_recipient")
    private Integer deleteRecipient;
    @JoinColumn(name = "to_user", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User toUser;
    @JoinColumn(name = "from_user", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User fromUser;

    @Transient
    private boolean delete;

    public Message() {
    }

    public Message(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getTitlePreview() {

        if (!UtilHelper.isStringEmpty(title)) {

            if (title.length() > 60) {
                return title.substring(0, 59).concat(" ...");
            } else {
                return title;
            }

        } else {
            return "";
        }
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public String getMessagePreview() {

        if (!UtilHelper.isStringEmpty(message)) {

            if (message.length() > 80) {
                return message.substring(0, 79).concat(" ...");
            } else {
                return message;
            }


        } else {
            return "";
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Integer messageRead) {
        this.messageRead = messageRead;
    }

    public Integer getDeleteRecipient() {
        return deleteRecipient;
    }

    public void setDeleteRecipient(Integer deleteRecipient) {
        this.deleteRecipient = deleteRecipient;
    }

    public Integer getDeleteSender() {
        return deleteSender;
    }

    public void setDeleteSender(Integer deleteSender) {
        this.deleteSender = deleteSender;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.metallium.core.entity.Message[id=" + id + "]";
    }
}
