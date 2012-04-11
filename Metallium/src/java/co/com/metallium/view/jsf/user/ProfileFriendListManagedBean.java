/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.user;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "profileFriendList")
@ViewScoped
public class ProfileFriendListManagedBean  implements Serializable {

    private static int ccPF = 0;
    private Integer profileId = null; //It indates which profile I must display
    private String profileNick = null; //It indates which profile I must display

 //   private User user = new User();
    private String searchQuery;
    @EJB
    private SearchService searchService;
    //====Start Pagination===================================================//
    private DataModel items = null;
    private PaginationHelper pagination;
    private UserFriendsSearchDTO searchCriteria = new UserFriendsSearchDTO(User.class);
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    //====End Pagination=====================================================//

    /** Creates a new instance of ProfileFriendListManagedBean */
    public ProfileFriendListManagedBean() {
    }

    /**
     * Point of Entry to this ManagedBean. (REQUEST)
     * @param profileNick
     */
    public void setProfileNick(String profileNick) {

        ccPF ++;
        if (UtilHelper.areStringsEqual(this.profileNick, profileNick)) {
            //Since this is called by the web page, I make sure its only called
            //when changes do happen.
            return;
        } else {
            this.profileNick = profileNick;
        }

        this.getSearchCriteria().setUserNick(profileNick);//Nick of the profile so we can search for the users friends.
        pagination = null; //THis is so, that the every time I find a new profile, the comments pagination start from the begining.
        recreateItemsModel();
        System.out.println("ccPF: " + ccPF);


    }

    public String getProfileNick() {
        return profileNick;
    }

    /**
     *
     * @param profileId
     */
    public void setProfileId(Integer profileId) {

            this.profileId = profileId;

//        ccPF ++;
//        if (UtilHelper.areIntegersEqual(this.profileId, profileId)) {
//            //Since this is called by the web page, I make sure its only called
//            //when changes do happen.
//            return;
//        } else {
//            this.profileId = profileId;
//        }
//
//        this.getSearchCriteria().setUserId(profileId);//Id of the profile so we can search for the users friends.
//        pagination = null; //THis is so, that the every time I find a new profile, the comments pagination start from the begining.
//        recreateItemsModel();
//        System.out.println("ccPF: " + ccPF);
    }

    public Integer getProfileId() {
        return profileId;
    }

    //================Start Pagination Logic ==================================//
    public PaginationHelper getPagination() {
        if (profileId == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_PROFILE_FRIENDS_LIST) {

                @Override
                public int getItemsCount() {

                    if (!didItCountItemsAlready) {
                        countItems = searchService.countQuery(getSearchCriteria());
                        didItCountItemsAlready = true;
                    }
                    return countItems;
                }

                @Override
                public DataModel createPageDataModel() {

                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    getSearchCriteria().setRange(range);
                    List<User> searchResult = searchService.findQueryByRange(getSearchCriteria());
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            if (profileId != null) {
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
        didItCountItemsAlready = false;  //PLEASE REMEMBER that this is the only place where I can set this variable to TRUE. Otherwise it will result in multiple invocations to the count query.
    }

    public UserFriendsSearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(UserFriendsSearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
    //================End Pagination Logic ====================================//
}
