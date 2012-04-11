/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.components.wall;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.UserWallPostTypeEnum;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.entity.UserWallPost;
import co.com.metallium.core.exception.BaseException;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.exception.UserWallException;
import co.com.metallium.core.service.ImageGalleryService;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.UserWallService;
import co.com.metallium.core.service.dto.search.UserWallSearchDTO;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.JsfUtil;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * 20110810
 * @author Ruben
 */
@ManagedBean(name = "userWallComp")
@NoneScoped
public class UserWallComp extends BaseManagedBean implements Serializable {

    private Integer userId;
    //Pagination Variables
    private boolean entityExistes = true;
    private DataModel items = null;
    private PaginationHelper pagination;
    private UserWallSearchDTO searchDTO = null;
    //These two parameters are to control the number of times the count command compareStates executed with each call.
    //so it optimizes it to call it just one time insted of 10 or more times.
    private int count = 0;
    private boolean didItCountAlready = false;
    private List<UserWallPost> entitiesFound = null;
    private UserWallPost selectedItem;
    //----------------------------User Wall Post-----------------------------//
    private UserWallPost userWallPost = new UserWallPost();
    @EJB
    private SearchService searchService;
    @EJB
    private UserWallService userWallService;
    @EJB
    private ImageGalleryService imageGalleryService;
    //----------------------------Add Edit Post-----------------------------//
    private InfoRender infoRender = new InfoRender();
    private boolean editImage = false; //Tells me if the user editing the wall uploaded a new image or not. I need to know in order to also update the image in the DB when he updates the post.

    public UserWallComp() {
    }

    public void initialize(Integer userId) {
        this.userId = userId;
        this.setSearchDTO(new UserWallSearchDTO(userId)); //I initialize the search criteria
    }

    public void valueChangedPostType(ValueChangeEvent event) {

        UserWallPostTypeEnum type = (UserWallPostTypeEnum) event.getNewValue();
        this.getInfoRender().initialize(type);
        this.cleanWallPostContent(type);
    }
    //==============================Pagination======================================//

    /**
     * Searches for Event Comments
     *
     * @return
     */
    public PaginationHelper getPagination() {

        if (DataHelper.isNull(this.userId)) {
            return null;
        }

        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS_COMMENTS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountAlready) {
                        count = searchService.countQuery(getSearchDTO());
                        didItCountAlready = true;
                    }
                    return count;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    UserWallSearchDTO search = getSearchDTO();
                    search.setRange(range);

                    entitiesFound = searchService.findQueryByRange(search);
                    return new ListDataModel(entitiesFound);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            //What I do is that if there is no News or what ever the page is supposed to display,
            //then I dont even bother to initialize the Pagination Object.
            if (isEntityExistes()) {
                items = getPagination().createPageDataModel(); //I initialize de Pagination Object to get results.
            }
        }
        return items;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public void recreateModel() {
        items = null;
        didItCountAlready = false;
    }

    //==============================User Wall Post===========================//
    /**
     * Awsome response from BalusC 
     * http://stackoverflow.com/questions/4994458/how-can-i-pass-a-parameter-to-a-commandlink-inside-a-datatable
     * 
     * @param userWallPost 
     */
    public void playVideo(UserWallPost userWallPost) {
        userWallPost.setPlayMedia(true);
    }

    public UserWallPost findPost(Integer postId, Integer userId) throws UserWallException {
        this.userWallPost = userWallService.findPostByPostIdAndUserId(postId, userId);
        return this.userWallPost;
    }

    public String cancelPost() {
        this.userWallPost = new UserWallPost();
        return null;
    }

    public String createPost() {
        return executeUpdate("create");
    }

    public String editPost() {
        return executeUpdate("edit");
    }

    public String deletePost() {
        return executeUpdate("delete");
    }

    private String executeUpdate(String accion) {
        String answer = Navegation.goToProfileRedirect;

        if (!"delete".equalsIgnoreCase(accion)) {
            this.validatePostUpdate();
        }

        if (JsfUtil.getMessageListSize() == 0) {
            try {

                if (this.isAuthenticated()) {

                    if ("create".equals(accion)) {
                        this.getUserWallPost().setUserId(this.getAuthenticaedUser().getId());
                        this.userWallService.createPost(userWallPost);

                    } else {

                        if (canIeditWallPostLogic(this.getUserWallPost())) {

                            if ("edit".equals(accion)) {
                                this.userWallService.editPost(userWallPost, editImage);

                            } else if ("delete".equals(accion)) {

                                UserWallPost auxPost = selectedItem;//(UserWallPost) getItems().getRowData();
                                this.userWallService.deletePost(auxPost);
                                this.recreateModel(); //I make sure that the table refreshes its items.
                                answer = Navegation.stayInSamePage;
                            }

                        } else {
                            throw new UserWallException("", null, applicationParameters.getResourceBundleMessage("common_info_restricted_operation"));
                        }

                    }

                } else {
                    throw new UserWallException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
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

    private void validatePostUpdate() {

        try {

            if (this.getInfoRender().getText()) {
                if (UtilHelper.isStringEmpty(this.getUserWallPost().getContent())) {
                    this.addFacesMsgFromProperties("wall_post_info_url_content_required");
                }
            } else if (this.getInfoRender().getImage()) {
                if (UtilHelper.isStringEmpty(this.getUserWallPost().getImage())) {
                    this.addFacesMsgFromProperties("wall_post_info_url_image_required");
                }
            } else if (this.getInfoRender().getLink()) {
                if (UtilHelper.isStringEmpty(this.getUserWallPost().getUrl())) {
                    this.addFacesMsgFromProperties("wall_post_info_url_link_required");
                }
            } else if (this.getInfoRender().getVideo()) {
                if (UtilHelper.isStringEmpty(this.getUserWallPost().getUrl())) {

                    //If the Post type is VIDEO, and no video input is entered, then I will check if 
                    //some text is entered. If text is entered, then I change the Post Type from VIDEO to TEXT 
                    if (StringUtils.isNotEmpty(this.getUserWallPost().getContent())) {
                        this.getUserWallPost().setType(UserWallPostTypeEnum.TEXT);
                    } else {
                        this.addFacesMsgFromProperties("wall_post_info_url_video_required");
                    }

                }
            }

        } catch (Exception ex) {
            this.manageException(ex);
        }

    }

    public void handleImageUpload(FileUploadEvent event) {

        if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }

        if (true) {
            //I can edit the profile

            try {

                UploadedFile uploadedFile = event.getFile();
                // Just to demonstrate what information you can get from the uploaded file.

                String contentType = uploadedFile.getContentType();
                String fileName = uploadedFile.getFileName(); // "image"
                long fileSize = uploadedFile.getSize();

                PairDTO pairDTO = this.imageGalleryService.uploadImageWall(
                        fileName, contentType, fileSize, uploadedFile.getInputstream());

                this.getUserWallPost().setImage(pairDTO.getOne());
                this.getUserWallPost().setImageThumbnail(pairDTO.getTwo());

                this.editImage = true;

                this.addFacesMsgFromProperties("image_gallery_info_uploaded_successfully", fileName);
            } catch (BaseException ex) {
                this.manageException(ex);
            } catch (Exception ex) {
                String msg = applicationParameters.getResourceBundleMessage("common_info_file_upload_failed");
                manageException(new GalleryException(ex.getMessage(), ex, msg));
            }


        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }
    }
    //==============================STUFF=======================//

    private void cleanWallPostContent(UserWallPostTypeEnum type) {


        if (UserWallPostTypeEnum.TEXT.equals(type)) {
        } else if (UserWallPostTypeEnum.LINK.equals(type)) {
            this.getUserWallPost().setContent(null);
        } else if (UserWallPostTypeEnum.IMAGE.equals(type)) {
        } else if (UserWallPostTypeEnum.VIDEO.equals(type)) {
        }

//        this.getUserWallPost().setUrl(null);
//        this.getUserWallPost().setImage(null);
//        this.getUserWallPost().setImageThumbnail(null);
//        this.getUserWallPost().setContent(null);

    }

    /**
     * Only two persons can editPost or deletePost this post.
     * The person who created it (Author).
     * An Administrator level 2 or greater.
     *
     */
    public boolean canIeditWallPostLogic(UserWallPost entity) {

        if (!this.isAuthenticated()) {
            return false;
        }

        boolean answer = false;

        User authUser = this.getAuthenticaedUser();
        Integer myId = authUser.getId();
        Integer author = entity.getUserId();
        if (UtilHelper.areIntegersEqual(myId, author)) {
            //I am the author.
            answer = true;
        } else {
            if (UserProfile.isAdministrator2(authUser.getProfile())) {
                //Im not the author, But I have the right to deletePost it.
                answer = true;
            }
        }
        return answer;
    }

    //==============================Getters & Setters =======================//
    public boolean isEntityExistes() {
        return entityExistes;
    }

    public void setEntityExistes(boolean entityExistes) {
        this.entityExistes = entityExistes;
    }

//    public int getCalmDownCounter() {
//        return calmDownCounter;
//    }
//
//    public void setCalmDownCounter(int calmDownCounter) {
//        this.calmDownCounter = calmDownCounter;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
    public List<UserWallPost> getWallPosts() {
        return entitiesFound;
    }

    public void setWallPosts(List<UserWallPost> userWallPosts) {
        this.entitiesFound = userWallPosts;
    }

    public UserWallSearchDTO getSearchDTO() {
        return searchDTO;
    }

    public void setSearchDTO(UserWallSearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    public int getCountComments() {
        return count;
    }

    public void setCountComments(int countComments) {
        this.count = countComments;
    }

    public UserWallPost getUserWallPost() {
        return userWallPost;
    }

    public void setUserWallPost(UserWallPost userWallPost) {
        this.userWallPost = userWallPost;
    }

    public UserWallPost getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(UserWallPost selectedItem) {
        this.selectedItem = selectedItem;
    }

    public InfoRender getInfoRender() {
        return infoRender;
    }

    public void setInfoRender(InfoRender infoRender) {
        this.infoRender = infoRender;
    }
}
