/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.forum;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.ForumState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.Forum;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.ForumException;
import co.com.metallium.core.service.ForumService;
import co.com.metallium.view.jsf.BaseManagedBean;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

/**
 * 20110528
 * @author Ruben
 */
@ManagedBean(name = "forumBase")
@NoneScoped
public class ForumBaseManagedBean extends BaseManagedBean implements Serializable {

    
    private Short category = null; //Category of the forum list I am going to retrieve.
    
    @EJB
    ForumService forumService;
    //These are my CACHE VALUES
    private Boolean canIEditForum = null;

    /** Creates a new instance of ForumBaseManagedBean */
    public ForumBaseManagedBean() {
    }

    
    public void refreshCacheVariables() {
        this.canIEditForum = null;
    }
    
    /**
     * Only two persons can edit or delete forums.
     * The person who created the forum.
     * An Administrator level 2 or greater.
     *
     * @param auxForum
     * @return
     * @throws ForumException
     */
    public boolean canIeditForumLogic(Forum auxForum) {

        if (!this.isAuthenticated()) {
            return false;
        }

        boolean answer = false;

        if (!ForumState.isDeleted(auxForum.getState())) {
            User authUser = this.getAuthenticaedUser();
            Integer myId = authUser.getId();
            Integer userCommentId = auxForum.getUser().getId();
            if (UtilHelper.areIntegersEqual(myId, userCommentId)) {
                //I am the author.
                answer = true;
            } else {
                if (UserProfile.isAdministrator2(authUser.getProfile())) {
                    //Im not the author of the comment, But I have the right to delete it.
                    answer = true;
                }
            }
        }
        return answer;
    }

    public ForumService getForumService() {
        return forumService;
    }

    public String goToForum() {
        return Navegation.goToForum;
    }

    public String goToForumList() {
        return Navegation.goToForumListRedirect;
    }

    public String goToForumCreate() {
        return Navegation.goToForumCreate;
    }

    public String goToForumEdit() {
        return Navegation.goToForumEdit;
    }
    
    //===========Getters and Setters ========================================//
    
    public ArrayList<PairDTO> getSelectItemForumCategory() {
        return applicationParameters.getSelectItemForumCategory();
    }
    
    public Boolean getCanIEditForum() {
        return canIEditForum;
    }

    public void setCanIEditForum(Boolean canIEditForum) {
        this.canIEditForum = canIEditForum;
    }
    
    public Short getCategory() {
        return category;
    }

    public void setCategory(Short category) {
        this.category = category;
    }
    
}
