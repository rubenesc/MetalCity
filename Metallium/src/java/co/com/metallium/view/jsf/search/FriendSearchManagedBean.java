/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.search;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.SearchConst;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.SearchHelper;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.GeneralSearchDTO;
import co.com.metallium.view.jsf.user.UserBaseManagedBean;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.search.SqlCondition;
import com.metallium.utils.utils.UtilHelper;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "friendSearch")
@ViewScoped
public class FriendSearchManagedBean extends UserBaseManagedBean {

    private User user = new User(666, "motherfucker");
    private String searchQuery;
    private String searchQueryRequest;
    private boolean basicSearch = true;
    @EJB
    private SearchService searchService;
    //====Start Pagination===================================================//
    private DataModel items = null;
    private PaginationHelper pagination;
    private GeneralSearchDTO searchCriteria = null;
    private int countItems = 0;
    private boolean didItCountItemsAlready = false;
    //====End Pagination=====================================================//
    private User selectedUser = null;
    private Integer profileId;

    /** Creates a new instance of FriendSearchManagedBean */
    public FriendSearchManagedBean() {
    }

    @PostConstruct
    public void initializeInbox() {
        searchCriteria = new GeneralSearchDTO("", SearchConst.entityProfile);
        pagination = null;
        recreateItemsModel();
    }

    public void setProfileId(Integer profileId) {
        //I set the profile who's Id I get in the request.
        this.profileId = profileId;

        if (profileId != null) {
            searchCriteria.setFriendsOfUserId(profileId);
        }

    }

    public Integer getProfileId() {
        return profileId;
    }

    public void showBasicSearch() {
        this.setBasicSearch(true);
        reinitializeSearchCriteria();
    }

    public void performSearch() {

        validateInput();

        if (isJsfMessageListEmpty()) {
            searchCriteria.setQuery(searchQuery);
            searchCriteria.setName(searchQuery);
            searchCriteria.setEmail(searchQuery);
            searchCriteria.setCity(searchQuery);

            //I set OR, because Im going to search the same query for
            searchCriteria.setSqlCondition(SqlCondition.OR);
            performNewSearch();
        }

    }

    private void reinitializeSearchCriteria() {
        String searchEntity = this.getSearchCriteria().getSearchEntity();
        this.setSearchCriteria(new GeneralSearchDTO("", searchEntity));
    }

    public boolean isCanIAddToFriends() {
        boolean answer = false;

        try {
            User auxUser = (User) getItems().getRowData();
            answer = canI_x_ToFriend(auxUser.getId(), UserCommon.FRIEND_ADD);
        } catch (Exception e) {
            //I dont fucking care !!!
        }

        return answer;
    }

    //================Start Pagination Logic ==================================//
    public PaginationHelper getPagination() {
        if (user == null || user.getId() == null) {
            return null;
        }


        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_GENERAL) {

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
            if (user != null && user.getId() != null) {
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

    private void performNewSearch() {
        recreateItemsModel();
        this.pagination = null;
    }

    public GeneralSearchDTO getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(GeneralSearchDTO searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    //================End Pagination Logic ====================================//
    //================Input validators ========================================//
    private void validateInput() {

        if (UtilHelper.isStringEmpty(this.getSearchQuery())) {
            this.addFacesMsgFromProperties("search_info_validate_write_something");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getSearchCriteria().getSearchEntity())) {
            this.addFacesMsgFromProperties("search_info_validate_select_option");
            return;
        }

        if (false && this.getSearchQuery().length() <= 3) {
            this.addFacesMsgFromProperties("search_info_validate_write_something_more");
            return;
        }

    }

    private void validateAdvancedInput() {

        if (UtilHelper.isStringEmpty(this.getSearchCriteria().getSearchEntity())) {
            this.addFacesMsgFromProperties("search_info_validate_select_option");
            return;
        }

        try {
            if (SearchHelper.isSearchCriteriaEmpty(this.getSearchCriteria())) {
                this.addFacesMsgFromProperties("search_info_validate_advanced_search_write_something");
            }
        } catch (Exception e) {
            manageException(e);
        }

    }

    //================Getters and Setters =====================================//
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQueryRequest() {
        return searchQueryRequest;
    }

    public void setSearchQueryRequest(String searchQueryRequest) {
        this.searchQuery = searchQueryRequest;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    public void setBasicSearch(boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public void selectUserToFriends() {
        try {
            selectedUser = (User) getItems().getRowData();
        } catch (Exception e) {
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
