/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.state.ForumState;
import co.com.metallium.core.entity.UserComments;
import co.com.metallium.core.exception.CommentCalmDownException;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.service.dto.search.CommentsSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.Assert;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ruben
 */
@Stateless
public class UserCommentsService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private NewsService newsService;
    @EJB
    private EventService eventService;
    @EJB
    private ForumService forumService;
    @EJB
    private SearchService searchService;

    public UserCommentsService() {
    }

    public void create(UserComments comment) throws CommentException, CommentCalmDownException, ValidationException {

        validateIfUserCanCreateComment(comment);

        comment.setComment(HtmlSanitizer.comment(comment.getComment())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(comment.getComment());

        try {

            processCommentUrl(comment);
            
            validateCreateInput(comment);

            comment.setDate(new Date());
            comment.setState(CommentState.ACTIVE);
            em.persist(comment);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new CommentException(e.getMessage(), e);
        }

        this.updateNumberOfComments(comment.getEntityId(), comment.getType());
    }

    private void validateCreateInput(UserComments comment) throws ValidationException {
        if (StringUtils.isBlank(comment.getUrl())) {
            if (StringUtils.isBlank(comment.getComment())) {
                String msg = applicationParameters.getResourceBundleMessage("news_info_comment_required");
                throw new ValidationException(msg, null, msg);
            }

        }
    }

    /**
     * Aux Method that process the info, if the Comment has a URL.
     * It verifies if this URL is valid and if it is, it will retrieve 
     * the info regarding it.
     * 
     * @param comment
     * @throws Exception 
     */
    private void processCommentUrl(UserComments comment) throws Exception {
        if (StringUtils.isNotBlank(comment.getUrl())) {
            String videoId = getYouTubeIdWrapper(comment.getUrl());

            if (videoId != null) {
                processMediaInfoComment(videoId, comment);
            }
            comment.setUrl(HtmlSanitizer.link(comment.getUrl())); //Clean HTML input.. Sanitize evil shit!!!
            comment.setMediaId(videoId);
        }
    }

    private void updateNumberOfComments(Integer entityId, String type) {
        try {
            CommentsSearchDTO criteria = new CommentsSearchDTO(entityId, CommentState.ACTIVE, type, UserComments.class);
            int numberOfComments = searchService.countQuery(criteria);

            if (CommentType.NEWS.equalsIgnoreCase(type)) {
                newsService.updateNumberComments(entityId, numberOfComments);
            } else if (CommentType.EVENT.equalsIgnoreCase(type)) {
                eventService.updateNumberComments(entityId, numberOfComments);
            } else if (CommentType.FORUM.equalsIgnoreCase(type)) {
                forumService.updateNumberComments(entityId, numberOfComments);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }

    }

    public void edit(UserComments comment) throws CommentException, ValidationException {

        comment.setComment(HtmlSanitizer.comment(comment.getComment())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(comment.getComment());

        try {
            
            processCommentUrl(comment);
            
            validateCreateInput(comment);

            em.merge(comment);
            
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new CommentException(e.getMessage(), e);
        }


    }

    public void remove(UserComments comment) {
        try {
            em.remove(em.merge(comment));
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
    }

    public UserComments findNewsCommentById(int id) throws CommentException {
        UserComments entity = null;

        try {
            entity = (UserComments) em.createNamedQuery("UserComments.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The user comment with id: " + id + ", could not be found in the system.";
                throw new CommentException(msg, ex);
            } else {
                throw new CommentException(ex.getMessage(), ex);
            }
        }
        return entity;
    }

    //@Interceptors(InterceptorX.class)
    public void deleteComment(UserComments entity) throws CommentException {

        UserComments entityToUpdate = this.findNewsCommentById(entity.getId());

        try {

            if (!CommentState.isDeleted(entity.getState())) {
                entityToUpdate.setState(CommentState.DELETED);
                em.merge(entityToUpdate);
            } else {
                LogHelper.makeLog("... Cant delete user comment because it's state its: " + entity.getState());
            }

            //Sinice I just deleted a comment, I have to update the number of active for that particular comment type (News, Events, etc)
            this.updateNumberOfComments(entity.getEntityId(), entity.getType());
        } catch (Exception e) {
            throw new CommentException(e.getMessage(), e);
        }

    }

    private void validateIfUserCanCreateComment(UserComments comment) throws CommentCalmDownException {

        int count = countCommentsInCertainTime(comment);
        if (count >= MetConfiguration.MAX_NUMBER_COMMENTS_IN_TIME) {
            String msg = applicationParameters.getResourceBundleMessage("comment_info_too_many_comments_in_short_time");
            throw new CommentCalmDownException(msg, null, msg);
        }
    }

    /*
     *
     * This counts the NEWS comments written for an especific NEWS.
     *
     * @Param CommentsSearchDTO criteriaDTO = it has just the "NEWS id" to search for
     * return int = number of comments
     */
    private int countCommentsInCertainTime(UserComments comment) {
        int answer = 0;
        try {

            Date dateValidation = MetUtilHelper.getCommentTimeValidationDate();
            answer = ((Number) em.createQuery("Select COUNT(DISTINCT n.id) "
                    + "From UserComments n "
                    + "Where n.entityId = :entityId "
                    + "and  n.user.id = :userId "
                    + "and  n.state = :state "
                    + "and  n.type = :type "
                    + "and  n.date >= :date ").setParameter("entityId", comment.getEntityId()).setParameter("userId", comment.getUser().getId()).setParameter("state", CommentState.ACTIVE).setParameter("type", comment.getType()).setParameter("date", dateValidation).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    /**
     * This deletes the message on the wall.
     *
     * @param commentId 
     * @throws CommentException
     */
    public void deleteComment_(UserComments comment) throws CommentException {

        try {
            Query q = em.createNamedQuery("UserComments.deleteComment");
            q.setParameter("id", comment.getId());
            q.executeUpdate();

            //Sinice I just deleted a comment, I have to update the number of active for that particular comment type (News, Events, etc)
            this.updateNumberOfComments(comment.getEntityId(), comment.getType());

        } catch (Exception e) {
            throw new CommentException(e);
        }
    }

    /**
     * This deletes all the messages one the wall. I am assuming that who ever
     * invokes this is the owner of the wall.
     *
     * @param entityId
     */
    public void deleteAllCommentsFromWall(Integer entityId) {

        try {
            Query q = em.createNamedQuery("UserComments.softDeleteAllCommentsFromWall");
            q.setParameter("state", ForumState.DELETED);
            q.setParameter("entityId", entityId);
            int updated = q.executeUpdate();
            LogHelper.makeLog("UserComments.softDeleteAllCommentsFromWall entityId: " + entityId + ", deleted comments: " + updated);
        } catch (Exception e) {
            //I cant do anything but log the exception.
            LogHelper.makeLog(e);
        }
    }

    /**
     * This deletes all the messages on the wall. I am assuming that who ever
     * invokes this is the owner of the wall.
     *
     * @param entityId
     * @param imageId
     */
    public void deleteAllCommentsFromImageWall(Integer entityId, String imageId) {

        try {
            Query q = em.createNamedQuery("UserComments.deleteAllCommentsFromImageWall");
            q.setParameter("entityId", entityId);
            q.setParameter("imageId", imageId);
            q.executeUpdate();
        } catch (Exception e) {
            //I cant do anything but log the exception.
            LogHelper.makeLog(e);
        }
    }
}
