/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.components; 

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.ForumCategoryStateEnum;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.entity.Forum;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.ForumSearchDTO;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20110526
 * @author Ruben
 */
@ManagedBean(name = "paginationComp")
@NoneScoped
public class PaginationComp extends BaseManagedBean implements Serializable {

    private DataModel items = null;
    private PaginationHelper pagination;
    private ForumSearchDTO searchDTO = new ForumSearchDTO();
    //These two parameters are to control the number of times the count command compareStates executed with each call.
    //so it optimizes it to call it just one time insted of 10 or more times.
    private int count = 0;
    private boolean didItCountAlready = false;
    private List<Forum> searchResult = null;
    //Comments Variables
    @EJB
    private SearchService searchService;

    /** Creates a new instance of PaginationComp */
    public PaginationComp() {
    }

    /**
     * This method sets a Network depending of the user who is browsing the events section.
     * The idea is that if a user is logged in the system and decides to go to the NEWS section
     * then he can see first the NEWS regarding his network (Is the network is has configured in his profile)
     *
     * If the user is not logged in then the default network will be selected.
     */
    public void initializeNetwork() {
        if (DataHelper.isNull(searchDTO.getNetwork())) {
            //If no Network is set we have to see which network applies for this user
            this.searchDTO.setNetwork(obtainCurrentUserNetwork());
        }
    }
    
    public void valueChangeListenerNetwork(ValueChangeEvent vce) {
        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.searchDTO.setNetwork((Integer) vce.getNewValue());
            } else {
                this.searchDTO.setNetwork(null);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.searchDTO.setNetwork(null);
        }

        this.recreateModel();
        pagination = null;

    }

    /**
     * Searches for News
     *
     * @return
     */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountAlready) {
                        //count the number of active NEWS to display in the index page
                        searchDTO.setState(NewsState.ACTIVE);
                        count = searchService.countQuery(searchDTO);
                        didItCountAlready = true;
                    }

                    return count;
                }

                @Override
                public DataModel createPageDataModel() {
                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    searchDTO.setRange(range);
                    searchDTO.setState(NewsState.ACTIVE);
                    //find the active NEWS to display in the index page
                    searchResult = searchService.findQueryByRange(searchDTO);
                    return new ListDataModel(searchResult);
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
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

    //=========================End News Pagination Logic=====================//
    private void recreateModel() {
        items = null;
        didItCountAlready = false;
    }

    //===================== Getters & Setters ============================//
    public ForumSearchDTO getSearchDTO() {
        return searchDTO;
    }

    public void setSearchDTO(ForumSearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }
}
