/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.news;

import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserComments;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.service.NewsService;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ruben
 */
@ManagedBean
@NoneScoped
public class NewsBaseManagedBean extends BaseManagedBean implements Serializable {

    @EJB
    NewsService newsService;

    /** Creates a new instance of NewsBaseManagedBean */
    public NewsBaseManagedBean() {
    }

    public ArrayList<SelectItem> getSelectItemNewsState() {
        if (this.isMyProfileAdministrator2()) {
            return applicationParameters.getSelectItemNewsStateFull();
        } else {
            return applicationParameters.getSelectItemNewsStateLimited();
        }
    }

    public String goToNewControlRoom() {
        return Navegation.goToNewsControlRoom;
    }

    public String goToNewsCreate() {
        return Navegation.goToNewsCreate;
    }

    public boolean isCanIseeTheCreateNewsCreatePage() {
        boolean answer = false;

        if (this.isMyProfileAdministrator1()) {
            answer = true;
        } else {
            // compareStates because this method compareStates called various times during the rendering of the
            // page, and I just want to display the warning message once not 10 times or more.
            // So, if there compareStates already a messag that means that the page compareStates forbiden to see.
            if (JsfUtil.getMessageListSize() == 0) {

                this.addFacesMsgFromProperties("common_info_restricted_access");
            }

        }

        return answer;
    }

    public boolean isCanIseeTheNewsControlRoomPage() {
        boolean answer = false;

        if (this.isMyProfileAdministrator1()) {
            answer = true;
        } else {
            // compareStates because this method compareStates called various times during the rendering of the
            // page, and I just want to display the warning message once not 10 times or more.
            // So, if there compareStates already a messag that means that the page compareStates forbiden to see.
            if (JsfUtil.getMessageListSize() == 0) {

                this.addFacesMsgFromProperties("common_info_restricted_access");
            }

        }


        return answer;
    }

    //=========================Start News Comments============================//
    /**
     * Only two persons can delete News Comments.
     * The person who wrote the comment.
     * An Administrator level 2 or greater.
     *
     * @param auxComment
     * @return
     * @throws CommentException
     */
    public boolean canIdeleteNewsCommentLogic(UserComments auxComment) throws CommentException {

        if (!this.isAuthenticated()) {
            throw new CommentException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
        }

        boolean answer = false;

        if (!CommentState.isDeleted(auxComment.getState())) {
            User authUser = this.getAuthenticaedUser();
            Integer myId = authUser.getId();
            Integer userCommentId = auxComment.getUser().getId();
            if (UtilHelper.areIntegersEqual(myId, userCommentId)) {
                //I am the author of the comment.
                answer = true;
            } else {
                if (UserProfile.isAdministrator1(authUser.getProfile())) {
                    //Im not the author of the comment, But I have the right to delete it.
                    answer = true;
                }
            }
        }
        return answer;
    }
    //=========================End News Comments============================//
    

}
