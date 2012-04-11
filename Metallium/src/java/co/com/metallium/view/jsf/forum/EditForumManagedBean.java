/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.forum;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Forum;
import co.com.metallium.core.entity.ForumCategory;
import co.com.metallium.core.exception.ForumException;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;

/**
 * 20111121
 * @author Ruben
 */
@ManagedBean(name = "editForum")
@ViewScoped
public class EditForumManagedBean extends ForumBaseManagedBean implements Serializable {

    private Integer id = null;
    private boolean exists = false;
    private Forum forum;

    /** Creates a new instance of EditForumManagedBean */
    public EditForumManagedBean() {
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadForumById() {

        String answer = Navegation.PRETTY_FORUM_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        refreshCacheVariables();//I must calculate my variables.

        if (this.id != null) {

            Forum forumToFind = null;
            try {

                forumToFind = this.getForumService().findForumById(this.id);
                this.setExists(true);
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

            this.setForum(forumToFind); //So, this object is set, so if i need to edit the forum I just edit it from here.
        }
        return answer;
    }

    /***
     * This method is called by Pretty Faces, when I go to the Create Forum page.
     *
     * @return String Navigation Page.
     */
    public String forumCreate() {

        String answer = Navegation.PRETTY_FORUM_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.
        answer = Navegation.PRETTY_OK; //The event was loaded correctly so I allow the user to go this news page.

        forum = new Forum();
        forum.setCategory(new ForumCategory(this.getCategory())); //This is so that when Im going to create a Category I dont get a Null pointer exception.

        return answer;
    }

    //=========================Create & Edit Forum ==========================//
    public boolean isCanIeditForum() {

        if (this.getCanIEditForum() == null) {
            boolean auxAnswer = false;
            try {
                auxAnswer = canIeditForumLogic(this.forum);
            } catch (Exception ex) {
            }
            this.setCanIEditForum(auxAnswer);
        }

        return this.getCanIEditForum();
    }

    public String cancelEditForum() {
        try {
            if (this.forum != null && StringUtils.isNotEmpty(this.getForum().getAlias())) {
                FacesUtils.redirect(MetUtilHelper.buildForumPageUrl(this.forum.getAlias()));
            }
        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }
        return Navegation.goToForumListRedirect;

    }

    public String createForum() {
        return executeUpdate("create");
    }

    public String editForum() {
        return executeUpdate("edit");
    }

    public String deleteForum() {
        return executeUpdate("delete");
    }

    private String executeUpdate(String accion) {
        String answer = Navegation.goToForumList;

        this.validateForumUpdate();

        if (JsfUtil.getMessageListSize() == 0) {
            try {

                if (this.isAuthenticated()) {

                    if ("create".equals(accion)) {
                        this.forum.setUser(this.getAuthenticaedUser());
                        this.forum.setNetwork(obtainCurrentUserNetwork());
                        this.forum = this.getForumService().createForum(this.forum);
                        this.addFacesMsgFromProperties("event_control_info_event_created");
                        answer = Navegation.goToForumList;
                        //Ok I want to send the user to the newly created forum, so I make this redirect
                        //invocation to the URL of the new Forum. It adds and additional trip to the client, but thats ok.
                        //It ignores the action I am sending, and will redirect to user to desired forum page
                        FacesUtils.redirect(MetUtilHelper.buildForumPageUrl(this.forum.getAlias()));
                    } else {

                        if (canIeditForumLogic(forum)) {

                            if ("edit".equals(accion)) {
                                this.forum = this.getForumService().editForum(this.forum);
                                answer = Navegation.goToForumList;
                                //Ok I want to send the user to the newly edited forum, so I make this redirect
                                //invocation to the URL of the new Forum. It adds and additional trip to the client, but thats ok.
                                //It ignores the action I am sending, and will redirect to user to desired forum page
                                FacesUtils.redirect(MetUtilHelper.buildForumPageUrl(this.forum.getAlias()));

                            } else if ("delete".equals(accion)) {
                                this.getForumService().deleteForum(this.forum);
                                this.addFacesMsgFromProperties("forum_info_delete_forum");
                                answer = Navegation.goToForumList;
                            }

                        } else {
                            throw new ForumException("", null, applicationParameters.getResourceBundleMessage("common_info_restricted_operation"));
                        }

                    }

                } else {
                    throw new ForumException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
                }

            } catch (Exception ex) {
                this.manageException(ex);
                answer = Navegation.stayInSamePage;
            }


        } else {
            answer = Navegation.stayInSamePage;

        }

        return answer;
    }

    private void validateForumUpdate() {

        if (StringUtils.isEmpty(forum.getTitle())) {
            this.addFacesMsgFromProperties("news_field_title");
            return;
        } else {
            if (DataHelper.isTooLong(forum.getTitle(), 200)) {
                this.addFacesMsgFromValidationProperties("validation_input_too_long", "forum_field_title");
                return;
            }
        }

        if (StringUtils.isEmpty(forum.getContent())) {
            this.addFacesMsgFromProperties("news_field_content");
            return;
        } else {
            if (DataHelper.isTooLong(forum.getContent(), 5000)) {
                this.addFacesMsgFromValidationProperties("validation_input_too_long", "forum_field_content");
                return;
            }
        }

    }

    //=============Getters and Setters ===================================//
    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
