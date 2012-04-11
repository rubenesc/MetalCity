/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.entity.UserWallComments;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.log.interceptor.InterceptorX;
import co.com.metallium.core.service.dto.search.UserWallCommentsSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ruben
 */
@Stateless
public class UserWallCommentsService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;

    public UserWallCommentsService() {
    }

    public void create(UserWallComments comment) throws CommentException, ValidationException {

        validateIfUserCanCreateComment(comment.getUserWallId(), comment.getUser().getId());

        comment.setComment(HtmlSanitizer.comment(comment.getComment())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(comment.getComment());

        comment.setDate(new Date());
        comment.setState(CommentState.ACTIVE);
        em.persist(comment);
    }

    public void edit(UserWallComments comment) throws ValidationException  {
        
        comment.setComment(HtmlSanitizer.comment(comment.getComment())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(comment.getComment());

        em.merge(comment);
    }

    public void remove(UserWallComments comment) {
        em.remove(em.merge(comment));
    }

    public UserWallComments findUserWallCommentById(int id) throws CommentException {
        UserWallComments entity = null;

        try {
            entity = (UserWallComments) em.createNamedQuery("UserWallComments.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The user wall comment with id: " + id + ", could not be found in the system.";
                throw new CommentException(msg, ex);
            } else {
                throw new CommentException(ex.getMessage(), ex);
            }
        }
        return entity;
    }

    public List<UserWallComments> findUserWallCommentsByRange(int[] range, UserWallCommentsSearchDTO criteriaDTO) {

        List<UserWallComments> answer = null;

        try {
            Query q = em.createNamedQuery("UserWallComments.findCommentsByUserWallAndState");
            q.setParameter("userWallId", criteriaDTO.getUserWallId());
            q.setParameter("state", criteriaDTO.getState());

            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            answer = q.getResultList();
        } catch (Exception e) {
            answer = new ArrayList<UserWallComments>();
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }


    /*
     *
     * This counts the comments written on a specific Wall
     *
     * @Param UserWallCommentsSearchDTO criteriaDTO = it has just the "user id" to search for
     * which compareStates the owner of the wall.
     * 
     * return int = number of comments
     */
    public int countCommentsByUserWall(UserWallCommentsSearchDTO criteriaDTO) {
        int answer = 0;
        try {

            answer = ((Number) em.createNamedQuery("UserWallComments.countCommentsByUserWallAndState").setParameter("userWallId", criteriaDTO.getUserWallId()).setParameter("state", criteriaDTO.getState()).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    @Interceptors(InterceptorX.class)
    public void deleteUserWallComment(UserWallComments entity) throws CommentException {

        UserWallComments entityToUpdate = this.findUserWallCommentById(entity.getId());

        try {

            if (!CommentState.isDeleted(entity.getState())) {
                entityToUpdate.setState(CommentState.DELETED);
                em.merge(entityToUpdate);
            } else {
                LogHelper.makeLog("... Cant delete comment because it's state its: " + entity.getState());
            }

        } catch (Exception e) {
            throw new CommentException(e.getMessage(), e);
        }

    }

    private void validateIfUserCanCreateComment(Integer wallId, Integer userId) throws CommentException {

        int count = countCommentsInCertainTime(wallId, userId);
        if (count >= MetConfiguration.MAX_NUMBER_COMMENTS_IN_TIME) {
            String msg = applicationParameters.getResourceBundleMessage("comment_info_too_many_comments_in_short_time");
            throw new CommentException(null, null, msg);
        }
    }

    /*
     *
     * This counts the news comments written for an especific news.
     *
     * @Param CriteriosBusquedaComentariosDTO criteriaDTO = it has just the "news id" to search for
     * return int = number of comments
     */
    private int countCommentsInCertainTime(Integer userWallId, Integer userId) {
        int answer = 0;
        try {

            Date dateValidation = MetUtilHelper.getCommentTimeValidationDate();
            answer = ((Number) em.createQuery("Select COUNT(DISTINCT n.id) "
                    + "From UserWallComments n "
                    + "Where n.userWallId = :userWallId "
                    + "and  n.user.id = :userId "
                    + "and  n.state = :state "
                    + "and  n.date >= :date ").setParameter("userWallId", userWallId).setParameter("userId", userId).setParameter("state", CommentState.ACTIVE).setParameter("date", dateValidation).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }
}
