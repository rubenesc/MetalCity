/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.images;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.CommentState;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.service.dto.search.ImageGallerySearchDTO;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.util.PaginationHelper;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20101129
 * @author Ruben
 */
@ManagedBean(name = "galleriesManagedBean")
@javax.faces.bean.ViewScoped
public class GalleriesManagedBean extends ImageGalleryBaseManagedBean implements Serializable {

    private boolean galleryExists; //Tells me if the gallery Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its banned or something.
    //=========start Image Pagination parameters
    private DataModel items = null; //These are the images items
    private PaginationHelper pagination;
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    //=========end Image Pagination parameters

    /** Creates a new instance of GalleryManagedBean */
    public GalleriesManagedBean() {
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadUserGalleries() {

        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        this.findUser(this.getUserNick());

        if (this.getUserId() != null) {
            //If the UserId is set then it is because we found a valid user.

            setAmItheOwnerOfTheGallery(isAmITheOwner(this.getUserId()));

            if (true || galleryExists) {
                //    Integer userId = this.getImageGallery().getUser().getId();

                //    users/user00/00/00/01/
                String userFilePath = MetUtilHelper.getRelativeUserPath(this.getUserId());
                //    users/user00/00/00/01/ + profile

                ImageGallerySearchDTO criteria = new ImageGallerySearchDTO(null, this.getUserId(), null, CommentState.ACTIVE);
                criteria.setUserPath(userFilePath);
                this.setSearchCriteria(criteria); //Profile owner of the WALL we are goint to interact with.
            }

            this.recreateItemsModel();
            answer = Navegation.PRETTY_OK;
        }


        return answer;

    }

    public boolean isGalleryExists() {

        if (this.getUserId() == null) { //&& this.getUser() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list is not empty is because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("profile_info_no_result_exception");
            }
            this.setGalleryExists(false);
        } else {
            this.setGalleryExists(true);
        }
        return this.galleryExists;
    }

    public void setGalleryExists(boolean galleryExists) {
        this.galleryExists = galleryExists;
    }

    //==========================Start Pagination=========================================//
    public PaginationHelper getPagination() {
        if (getUserId() == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_IMAGE_GALLERIES) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready) {

                        if (isAmItheOwnerOfTheGallery()) {
                            countItems = getImageGalleryService().countGalleriesWhereUserIsOwner(getSearchCriteria());
                        } else {
                            countItems = getImageGalleryService().countGalleriesWhereUserIsAnonymous(getSearchCriteria());
                        }
                        didItCountItemsAlready = true;
                    }
                    return countItems;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    List<ImageGallery> list = null;
                    if (isAmItheOwnerOfTheGallery()) {
                        list = getImageGalleryService().findUserGalleriesByRangeWhereUserIsOwner(range, getSearchCriteria());
                    } else {
                        list = getImageGalleryService().findUserGalleriesByRangeWhereUserIsAnonymous(range, getSearchCriteria());
                    }

                    return new ListDataModel(list);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            if (getUserId() != null) {
                items = getPagination().createPageDataModel();
            }
        }
        return items;
    }

    public String next() {
        getPagination().nextPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateItemsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public void recreateItemsModel() {
        items = null;
        didItCountItemsAlready = false;
    }
    //==========================End Pagination=========================================//
}
