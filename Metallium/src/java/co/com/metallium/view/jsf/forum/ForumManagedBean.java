/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.forum;

import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.entity.Forum;
import co.com.metallium.core.entity.ForumCategory;
import co.com.metallium.core.exception.ForumException;
import co.com.metallium.core.service.dto.search.ForumSearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.view.jsf.components.CommentsComp;
import co.com.metallium.view.jsf.components.PaginationComp;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;

/**
 * 20110525
 * @author Ruben
 */
@ManagedBean(name = "forum")
@ViewScoped
public class ForumManagedBean extends ForumBaseManagedBean implements Serializable {

    private String alias = null;
    private Integer id = null;
    private boolean exists = false;
    private Forum current;
    @ManagedProperty(value = "#{commentsComp}")
    private CommentsComp commentComp;
    @ManagedProperty(value = "#{paginationComp}")
    private PaginationComp paginationComp;
    private DataModel categories;

    //aux variables
    private ForumCategory forumCategory;
    
    
    /** Creates a new instance of ForumManagedBean */
    public ForumManagedBean() {
    }

    /***
     * This method is called by Pretty Faces.
     * It loads the forum categories
     *
     * @return String Navigation Page.
     */
    public String loadForumCategories() {
        String answer = Navegation.PRETTY_FORUM_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        if (categories == null) {
            categories = new ListDataModel(ApplicationParameters.getForumCategoryList());
        }
        answer = Navegation.PRETTY_OK; //Everything went well.

        return answer;

    }

    public String loadForumList() {
        String answer = Navegation.PRETTY_FORUM_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        if (this.getCategory() != null) {
            forumCategory = ApplicationParameters.getForumCategory(this.getCategory());
            ForumSearchDTO searchDTO = new ForumSearchDTO();
            searchDTO.setState(NewsState.ACTIVE);
            searchDTO.setCategory(this.getCategory());
            this.paginationComp.setSearchDTO(searchDTO);
            
            answer = Navegation.PRETTY_OK; //Everything went well.

        }


        return answer;

    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadForumByAlias() {

        String answer = Navegation.PRETTY_FORUM_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        Forum forumToFind = null;

        refreshCacheVariables();//I must calculate my variables.

        if (StringUtils.isNotBlank(this.alias)) {
            try {

                forumToFind = this.getForumService().findForumByAlias(this.alias);
                this.setId(forumToFind.getId());
                this.setExists(true);
                this.setCategory(forumToFind.getCategory().getId());

                //Ok finaly after I looked up the forum and I know it exists then I initialize the comments component.
                this.commentComp.initialize(CommentType.FORUM, id);
                this.paginationComp.initializeNetwork();

                answer = Navegation.PRETTY_OK; //The event was loaded correctly so I allow the user to go this news page.

            } catch (Exception ex) {
                if (ex.getCause() instanceof NoResultException) {
                    this.addFacesMsgFromProperties("search_info_no_results");
                    this.setExists(false);
                } else {
                    this.manageException(ex);
                    this.setExists(false);
                }
            }
        }

        this.setCurrent(forumToFind);

        return answer;
    }

    public boolean isCanIeditForum() {

        if (this.getCanIEditForum() == null) {
            boolean auxAnswer = false;
            try {
                auxAnswer = canIeditForumLogic(this.current);
            } catch (Exception ex) {
            }
            this.setCanIEditForum(auxAnswer);
        }

        return this.getCanIEditForum();
    }

    public String deleteForum() {

        String answer = Navegation.goToForumList;

        try {

            if (isCanIeditForum()) {

                this.getForumService().deleteForum(this.current);
                this.addFacesMsgFromProperties("forum_info_delete_forum");
                answer = Navegation.goToForumList;

            } else {
                throw new ForumException("", null, applicationParameters.getResourceBundleMessage("common_info_restricted_operation"));
            }

        } catch (Exception ex) {
            this.manageException(ex);
            answer = Navegation.stayInSamePage;
        }

        return answer;
    }

    //=========================Getters & Setters============================//
    public Forum getCurrent() {
        return current;
    }

    public void setCurrent(Forum current) {
        this.current = current;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public CommentsComp getCommentComp() {
        return commentComp;
    }

    public void setCommentComp(CommentsComp commentComp) {
        this.commentComp = commentComp;
    }

    public PaginationComp getPaginationComp() {
        return paginationComp;
    }

    public void setPaginationComp(PaginationComp paginationComp) {
        this.paginationComp = paginationComp;
    }

    public DataModel getCategories() {
        return categories;
    }

    public void setCategories(DataModel categories) {
        this.categories = categories;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }
    
}
