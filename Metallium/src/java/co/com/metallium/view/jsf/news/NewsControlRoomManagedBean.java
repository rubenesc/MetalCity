/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.news;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.entity.News;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.NewsSearchDTO;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * 20101111
 * @author Ruben
 */
@ManagedBean(name = "newsControlRoomManagedBean")
@ViewScoped
public class NewsControlRoomManagedBean extends NewsBaseManagedBean implements Serializable {

    private News currentNews;
    private int selectedItemIndex;
    private DataModel items = null;
    private NewsSearchDTO criteria = new NewsSearchDTO();
    private PaginationHelper pagination;
    private int count = 0;
    private boolean didItCountAlready = false;
    @EJB
    private SearchService searchService;

    /** Creates a new instance of NewsControlRoomManagedBean */
    public NewsControlRoomManagedBean() {
       // criteria.setState(NewsState.PENDING);  //If uncommented the "News Control Room" page comes with News in Pending state.
    }

    public void valueChangeListenerNetwork(ValueChangeEvent vce) {
        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.criteria.setNetwork((Integer) vce.getNewValue());
            } else {
                this.criteria.setNetwork(null);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.criteria.setNetwork(null);
        }

        searchForNews();

    }

    public void valueChangeListenerState(ValueChangeEvent vce) {

        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.criteria.setState(vce.getNewValue().toString());
            } else {
                this.criteria.setState("");
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.criteria.setState("");
        }

        searchForNews();
    }

    private void searchForNews() {
        this.recreateNewsModel();
        pagination = null;
    }

    public News getSelected() {
        if (currentNews == null) {
            currentNews = new News();
            selectedItemIndex = -1;
        }
        return currentNews;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS_CONTROL_ROOM) {

                @Override
                public int getItemsCount() {

                    if (!didItCountAlready) {
                        count = searchService.countQuery(criteria);
                        didItCountAlready = true;
                    }

                    return count;
                }

                @Override
                public DataModel createPageDataModel() {
                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    criteria.setRange(range);
                    List<News> searchResult = searchService.findQueryByRange(criteria);
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
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previous() {
        getPagination().previousPage();
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    private void recreateNewsModel() {
        items = null;
        didItCountAlready = false;
    }

    //======= News Actions - Start These methods should be called from the newsActions1.xhtml =======
    public String activateNews() {
        String answer = Navegation.stayInSamePage;

        try {
            currentNews = (News) getItems().getRowData();
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            this.newsService.activateNews(currentNews);
            this.recreateNewsModel();
            this.addFacesMsgFromProperties("news_control_info_news_activated");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public String deleteNews() {
        String answer = Navegation.stayInSamePage;

        try {
            currentNews = (News) getItems().getRowData();
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            this.newsService.deleteNews(currentNews);
            this.recreateNewsModel();
            this.addFacesMsgFromProperties("news_control_info_news_deleted");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }
    //======= News Actions - End ========================================================

    public NewsSearchDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(NewsSearchDTO criteria) {
        this.criteria = criteria;
    }

}
