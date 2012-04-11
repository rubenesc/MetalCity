/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.ForumState;
import co.com.metallium.core.entity.Forum;
import co.com.metallium.core.entity.ForumCategory;
import co.com.metallium.core.exception.ForumException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.util.ApplicationParameters;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * 20110526
 * @author Ruben
 */
@Stateless
public class ForumService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private ApplicationParameters applicationParameters;
    @EJB
    private UserCommentsService userCommentsService;
    @EJB
    private GeneralService generalService;

    public ForumService() {
    }

    public void edit(Forum entity) {
        em.merge(entity);
    }

    /**
     * Creates a new Forum.
     * @param entity
     * @throws ForumException
     */
    public Forum createForum(Forum entity) throws ForumException, ValidationException {

        entity.setContent(HtmlSanitizer.comment(entity.getContent())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(entity.getContent());

        try {

            ForumCategory newCatetory = em.find(ForumCategory.class, entity.getCategory().getId());
            entity.setCategory(newCatetory);

            entity.setState(ForumState.ACTIVE);
            entity.setDate(new Date());
            entity.setNumberComments(0);
            String titleAlias = this.generalService.generateAliasForTitle(entity.getTitle(), Forum.namedQueryCountAlias, true, null);
            entity.setAlias(titleAlias);

            em.persist(entity);
            em.flush();

        } catch (Exception e) {
            throw new ForumException(e);
        }

        return entity;
    }

    public Forum editForum(Forum forum) throws ForumException, ValidationException {

        Forum forumToEdit = this.findById(forum.getId());

        forumToEdit.setContent(HtmlSanitizer.comment(forum.getContent())); //Clean HTML input.. Sanitize evil shit!!!
        validateCommentLength(forumToEdit.getContent());

        try {

            if (forumToEdit.getCategory().getId().compareTo(forum.getCategory().getId()) != 0) {
                //If the Forum Category changed then we go to the process of updating the category.
                ForumCategory newCatetory = em.find(ForumCategory.class, forum.getCategory().getId());
                forumToEdit.setCategory(newCatetory);
            }

            forumToEdit.setState(forumToEdit.getState());
            forumToEdit.setNetwork(forum.getNetwork());

            if (UtilHelper.areStringsNotEqual(forumToEdit.getTitle(), forum.getTitle().trim())) {
                //Only if the Title was modified do I set the new title and generate
                //a new alias.
                forumToEdit.setTitle(forum.getTitle().trim());

                String titleAlias = this.generalService.generateAliasForTitle(forum.getTitle(), Forum.namedQueryCountAlias, false, forumToEdit.getId());
                forumToEdit.setAlias(titleAlias);
            }

            em.merge(forumToEdit);
            em.flush();

        } catch (Exception e) {
            throw new ForumException(e);
        }

        return forumToEdit;
    }

    public Forum deleteForum(Forum entity) throws ForumException {

        Forum entityToUpdate = this.findById(entity.getId());

        try {
            if (!ForumState.isDeleted(entity.getState())) {
                entityToUpdate.setState(ForumState.DELETED);
                em.merge(entityToUpdate);

                userCommentsService.deleteAllCommentsFromWall(entityToUpdate.getId());

            }
            /** To Do
            Mark all coments of the forum inactive?
            userCommentsService.deleteAllCommentsFromWall(entityToUpdate.getId());
             **/
        } catch (Exception e) {
            throw new ForumException(e.getMessage(), e);
        }

        return entity;
    }

    /**
     * Finds an Active forum based on the given Alias.
     * 
     * @param alias
     * @return
     * @throws ForumException 
     */
    public Forum findForumByAlias(String alias) throws ForumException {
        Forum answer = null;

        try {
            answer = (Forum) em.createNamedQuery("Forum.findByAlias").setParameter("alias", alias).setParameter("state", ForumState.ACTIVE).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The forum with alias: ".concat(alias).concat(", could not be found in the system.");
                throw new ForumException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new ForumException(ex.getMessage(), ex);
            }
        }
        return answer;
    }

    /**
     * Finds an Active forum based on the forums id
     * 
     * @param id
     * @return
     * @throws ForumException 
     */
    public Forum findForumById(Integer id) throws ForumException {
        Forum answer = null;

        try {
            answer = (Forum) em.createNamedQuery("Forum.findByIdAndState").setParameter("id", id).setParameter("state", ForumState.ACTIVE).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The forum with id: ".concat(id.toString()).concat(", could not be found in the system.");
                throw new ForumException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new ForumException(ex.getMessage(), ex);
            }
        }
        return answer;
    }

    /**
     * Fins a forum based on the id of the forum
     * 
     * @param id
     * @return
     * @throws ForumException 
     */
    public Forum findById(Integer id) throws ForumException {
        Forum answer = null;

        try {
            answer = (Forum) em.createNamedQuery("Forum.findById").setParameter("id", id).getSingleResult();

        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The forum with id: " + id + ", could not be found in the system.";
                throw new ForumException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new ForumException(ex.getMessage(), ex);
            }
        }
        return answer;
    }

    public void updateNumberComments(Integer id, int numberOfComments) throws ForumException {
        Forum entityToUpdate = this.findById(id);
        entityToUpdate.setNumberComments(numberOfComments);
        this.edit(entityToUpdate);
    }
}
