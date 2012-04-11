/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.components;

import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserComments;
import co.com.metallium.core.exception.CommentCalmDownException;
import co.com.metallium.core.exception.CommentException;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.UserCommentsService;
import co.com.metallium.core.service.dto.search.CommentsSearchDTO;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "commentsComp")
@NoneScoped
public class CommentsComp extends BaseManagedBean implements Serializable {

    private String type = null;
    private Integer entityId = null;
    private String imageId = null;
    //Pagination Variables
    private boolean entityExistes = true;
    private DataModel itemsComments = null;
    private PaginationHelper paginationComments;
    private CommentsSearchDTO commentsSearchDTO = new CommentsSearchDTO();
    //These two parameters are to control the number of times the count command compareStates executed with each call.
    //so it optimizes it to call it just one time insted of 10 or more times.
    private int countComments = 0;
    private boolean didItCountCommentsAlready = false;
    private List<UserComments> commentsFound = null;
    //Comments Variables
    private String comment = "";
    private String url;
    private int calmDownCounter = 0; //Counts how many time the user performs Stupid things, like trying to write a comment alot of times when he cant and has been warned not to do soy. So I count the times here until he reaches a limit.
    @EJB
    private SearchService searchService;
    @EJB
    UserCommentsService userCommentsService;

    public CommentsComp() {
    }

    public void initialize(String type, Integer entityId) {
        this.type = type;
        this.entityId = entityId;
        this.setCommentsSearchDTO(new CommentsSearchDTO(entityId, CommentState.ACTIVE, type, UserComments.class)); //I initialize the search criteria
    }

    public void initialize(String type, Integer entityId, String imageId) {
        this.type = type;
        this.entityId = entityId;
        this.imageId = imageId;
        this.setCommentsSearchDTO(new CommentsSearchDTO(entityId, imageId, CommentState.ACTIVE, type, UserComments.class)); //I initialize the search criteria
    }

    //==============================ONE======================================//
    /**
     * Searches for Event Comments
     *
     * @return
     */
    public PaginationHelper getPaginationComments() {

        if (DataHelper.isNull(this.entityId)) {
            return null;
        }

        if (paginationComments == null) {
            paginationComments = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS_COMMENTS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountCommentsAlready) {
                        countComments = searchService.countQuery(getCommentsSearchDTO());
                        didItCountCommentsAlready = true;
                    }
                    return countComments;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    CommentsSearchDTO search = getCommentsSearchDTO();
                    search.setRange(range);
                    //This finds the NEWS comments written for an especific EVENT
                    commentsFound = searchService.findQueryByRange(search);
                    return new ListDataModel(commentsFound);
                }
            };
        }
        return paginationComments;
    }

    public DataModel getItemsComments() {
        if (itemsComments == null) {
            //What I do is that if there is no News or what ever the page is supposed to display,
            //then I dont even bother to initialize the Pagination Object.
            if (isEntityExistes()) {
                itemsComments = getPaginationComments().createPageDataModel(); //I initialize de Pagination Object to get results.
            }
        }
        return itemsComments;
    }

    public String nextComments() {
        getPaginationComments().nextPage();
        recreateCommentsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previousComments() {
        getPaginationComments().previousPage();
        recreateCommentsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public void recreateCommentsModel() {
        itemsComments = null;
        didItCountCommentsAlready = false;
    }

    /**
     * This is when in a gallery someone selects a the next image, so this will
     * also restart the pagination. If not then the next image comments wall pagination could start
     * say in 80 and not in 1 for example. This this makes it always start in 1.
     */
    public void recreateCommentsModelImage() {
        recreateCommentsModel();
        paginationComments = null;
    }

    //==============================TWO=====================================//
    /**
     * This aux method just deals with what to do when you get a CalmDownException
     * and what it means is that a user is trying to post comments on a wall or something
     * to quickly. So he has to calm down.
     *
     * So, to calm him down, we count how many warnings has he received, and if he
     * reaches the limit, we send him to the banned page. This page will take care
     * of the user, like getting info on who he is, and banning him temporarily.
     *
     */
    private String manageCalmDownException(CommentCalmDownException ex) {
        String answer = Navegation.stayInSamePage;
        this.addFacesMsg(ex.getMessage());
        this.setComment(""); //I erase the bastards comment so he won't be able to send it immediately
        calmDownCounter++;
        if (calmDownCounter >= MetConfiguration.CALM_DOWN_EXCEPTION_LIMIT) {
            //I ban the guy.
            answer = Navegation.sendToBannedPage;
        }
        return answer;
    }

    public void playVideo(UserComments comment) {
        comment.setPlayMedia(true);
    }

    /**
     * This a wrapper of the method "publishComment()" because this method
     * takes the parameter ActionEvent.
     *
     * It takes it because when I refactored the JSF page that used to invoke the action "publishComment()"
     * a JSF Composite Component, I was forced to invoke it with this extra parameter, from the refactored component.
     * So I just made a wrapper for the method and thats it.
     *
     * @param evt
     * @return
     */
    public String publishComment() {
        String answer = Navegation.stayInSamePage;

        this.validateComment();

        if (isJsfMessageListEmpty()) {
            try {
                if (isAuthenticated()) {
                    UserComments newComment = new UserComments();
                    newComment.setType(this.type);
                    newComment.setComment(this.getComment());
                    newComment.setUrl(this.getUrl());
                    newComment.setEntityId(this.entityId);

                    if (CommentType.IMAGE.equals(this.type)) {
                        newComment.setImageId(this.imageId);
                    }

                    newComment.setUser(new User(this.getAuthenticaedUser().getId()));
                    this.userCommentsService.create(newComment);
                    this.recreateCommentsModel();
                    getPaginationComments();
                    this.setComment(""); //Once I publish the comment I clean it up on the web page.
                    this.setUrl("");
                } else {
                    throw new CommentException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
                }
            } catch (CommentCalmDownException ex) {
                
                answer = manageCalmDownException(ex);

            } catch (Exception ex) {
                answer = Navegation.stayInSamePage;
                this.manageException(ex);
            }
        }

        return answer;
    }

    private void validateComment() {

        try {

            if (StringUtils.isBlank(this.getUrl())) {
                //If the URL is blank, then I have to validate that they at least published a coment.
                if (StringUtils.isBlank(this.getComment())) {
                    this.addFacesMsgFromProperties("news_info_comment_required");
                }

            }
        } catch (Exception ex) {
            this.manageException(ex);
        }

    }

    public String deleteComment() {
        String answer = Navegation.stayInSamePage;

        try {
            UserComments auxComment = (UserComments) getItemsComments().getRowData();
            //I verify again that it compareStates a leagal operation
            if (canIdeleteCommentLogic(auxComment)) {
                this.userCommentsService.deleteComment(auxComment);
                this.recreateCommentsModel();
                this.addFacesMsgFromProperties("comment_deleted");
            } else {
                throw new CommentException("", null, applicationParameters.getResourceBundleMessage("common_info_restricted_operation"));
            }
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public void removeComments(Integer entityId, String imageId) {
        userCommentsService.deleteAllCommentsFromImageWall(entityId, imageId);
        this.recreateCommentsModel();
    }

    public boolean isCanIdeleteComment() {
        boolean answer = false;

        try {
            UserComments auxComment = (UserComments) getItemsComments().getRowData();
            answer = canIdeleteCommentLogic(auxComment);
        } catch (Exception ex) {
        }

        return answer;
    }

    /**
     * Only two persons can delete Event Comments.
     * The person who wrote the comment.
     * An Administrator level 2 or greater.
     *
     * @param auxComment
     * @return
     * @throws CommentException
     */
    public boolean canIdeleteCommentLogic(UserComments auxComment) {

        if (!this.isAuthenticated()) {
            return false;
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

    /**
     * ??? I see no point in this
     * @return
     *
     */
    public boolean isCanIActivateComment() {
        boolean answer = false;

        try {
            if (this.isAuthenticated()) {
                UserComments auxComment = (UserComments) getItemsComments().getRowData();
                //I can only delete a comment that compareStates ACTIVE
                //and that compareStates Mine, or If Im an administrator 2.
                if (CommentState.isActive(auxComment.getState())) {
                    Integer myId = this.getAuthenticaedUser().getId();
                    Integer userCommentId = auxComment.getUser().getId();

                    if (UtilHelper.areIntegersEqual(myId, userCommentId)) {
                        //I am the author of the comment.
                        answer = true;
                    } else {
                        if (UserProfile.isAdministrator1(this.getAuthenticaedUser().getProfile())) {
                            //Im not the author of the comment, But I have the right to delete it.
                            answer = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    //==================Getters & Setters=====================================//
    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
        this.getCommentsSearchDTO().setImageId(imageId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEntityExistes() {
        return entityExistes;
    }

    public void setEntityExistes(boolean entityExistes) {
        this.entityExistes = entityExistes;
    }

    public int getCalmDownCounter() {
        return calmDownCounter;
    }

    public void setCalmDownCounter(int calmDownCounter) {
        this.calmDownCounter = calmDownCounter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UserComments> getCommentsFound() {
        return commentsFound;
    }

    public void setCommentsFound(List<UserComments> commentsFound) {
        this.commentsFound = commentsFound;
    }

    public CommentsSearchDTO getCommentsSearchDTO() {
        return commentsSearchDTO;
    }

    public void setCommentsSearchDTO(CommentsSearchDTO commentsSearchDTO) {
        this.commentsSearchDTO = commentsSearchDTO;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }
}
