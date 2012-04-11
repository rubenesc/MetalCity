/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.news;

import co.com.metallium.core.constants.CommentType;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.entity.News;
import co.com.metallium.core.service.NewsService;
import co.com.metallium.core.service.SearchService;
import co.com.metallium.core.service.dto.search.NewsSearchDTO;
import co.com.metallium.view.jsf.components.CommentsComp;
import co.com.metallium.view.util.PaginationHelper;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.NoResultException;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "newsManagedBean")
@ViewScoped
public class NewsManagedBean extends NewsBaseManagedBean implements Serializable {

    private String newsAlias = null;
    private Integer newsId = null;
    private boolean newsExists = false;
    private News currentNews;
    //Pagination Variables
    private DataModel itemsNews = null;
    private PaginationHelper paginationNews;
    private NewsSearchDTO newsSearchDTO = new NewsSearchDTO();
    //These two parameters are to control the number of times the count command compareStates executed with each call.
    //so it optimizes it to call it just one time insted of 10 or more times.
    private int countNews = 0;
    private boolean didItCountNewsAlready = false;
    private List<News> searchResult = null;
    //Comments Variables
    @EJB
    private SearchService searchService;
    @ManagedProperty(value = "#{commentsComp}")
    private CommentsComp commentComp;

    /** Creates a new instance of NewsManagedBean */
    public NewsManagedBean() {
        initializeNewsNetwork();
    }

    public String getNewsAlias() {
        return newsAlias;
    }

    public void setNewsAlias(String newsAlias) {
        this.newsAlias = newsAlias;
    }

    /**
     * This method is the Point of Entry for the page NEWS.xhtml
     * @param newsId
     */
    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String loadNewsByAlias() {

        String answer = Navegation.PRETTY_NEWS_LIST; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        News newsToFind = null;
        try {
            newsToFind = this.getNewsService().findNewsByAlias(this.newsAlias);

            this.setNewsId(newsToFind.getId());
            this.setNewsExists(true);

            //Ok finaly after I looked up the news and I know it exists then I initialize the comments component.
            this.commentComp.initialize(CommentType.NEWS, newsId);

            answer = Navegation.PRETTY_OK; //The news was loaded correctly so I allow the user to go this news page.

        } catch (Exception ex) {
            if (ex.getCause() instanceof NoResultException) {
                this.addFacesMsgFromProperties("search_info_no_results");
                this.setNewsExists(false);
            } else {
                this.manageException(ex);
                this.setNewsExists(false);
            }
        }
        this.setCurrentNews(newsToFind);

        return answer;
    }

    //=========================Start News Pagination Logic=====================//
    public void valueChangeListenerNetwork(ValueChangeEvent vce) {
        if (!vce.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
            vce.setPhaseId(PhaseId.INVOKE_APPLICATION);
            vce.queue();
            return;
        }

        try {
            if (vce.getNewValue() != null) {
                this.newsSearchDTO.setNetwork((Integer) vce.getNewValue());
            } else {
                this.newsSearchDTO.setNetwork(null);
            }
        } catch (Exception e) {
            LogHelper.makeLog(e);
            this.newsSearchDTO.setNetwork(null);
        }

        this.recreateNewsModel();
        paginationNews = null;

    }

    /**
     * Searches for News
     *
     * @return
     */
    public PaginationHelper getPaginationNews() {
        if (paginationNews == null) {
            paginationNews = new PaginationHelper(MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS) {

                @Override
                public int getItemsCount() {

                    if (!didItCountNewsAlready) {
                        //count the number of active NEWS to display in the index page
                        newsSearchDTO.setState(NewsState.ACTIVE);
                        countNews = searchService.countQuery(newsSearchDTO);
                        didItCountNewsAlready = true;
                    }

                    return countNews;
                }

                @Override
                public DataModel createPageDataModel() {
                    int[] range = new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()};
                    newsSearchDTO.setRange(range);
                    newsSearchDTO.setState(NewsState.ACTIVE);
                    //find the active NEWS to display in the index page
                    searchResult = searchService.findQueryByRange(newsSearchDTO);
                    return new ListDataModel(searchResult);
                }
            };
        }
        return paginationNews;
    }

    public DataModel getItemsNews() {
        if (itemsNews == null) {
            itemsNews = getPaginationNews().createPageDataModel();
        }
        return itemsNews;
    }

    public String nextNews() {
        getPaginationNews().nextPage();
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    public String previousNews() {
        getPaginationNews().previousPage();
        recreateNewsModel();
        return Navegation.stayInSamePage; //I Stay in the same page
    }

    //=========================End News Pagination Logic=====================//
    private void recreateNewsModel() {
        itemsNews = null;
        didItCountNewsAlready = false;
    }

    public News getCurrentNews() {
        return currentNews;
    }

    public void setCurrentNews(News currentNews) {
        this.currentNews = currentNews;
    }

    public NewsSearchDTO getNewsSearchDTO() {
        return newsSearchDTO;
    }

    public void setNewsSearchDTO(NewsSearchDTO newsSearchDTO) {
        this.newsSearchDTO = newsSearchDTO;
    }

    private NewsService getNewsService() {
        return newsService;
    }

    public boolean isNewsExists() {
        if (this.getNewsId() == null) {
            if (this.isJsfMessageListEmpty()) {
                //If the msg list compareStates not empty compareStates because I already put the corresponding
                //msg I need to show, so this method could get called various times and I dont want to
                //show the msg repeatedly
                this.addFacesMsgFromProperties("news_info_no_result_exception");
            }
            this.setNewsExists(false);

        }

        return this.newsExists;
    }

    public void setNewsExists(boolean newsExists) {
        this.newsExists = newsExists;
    }

    /**
     * This method sets a Network depending of the user who is browsing the NEWS section.
     * The idea is that if a user is logged in the system and decides to go to the NEWS section
     * then he can see first the NEWS regarding his network (Is the network is has configured in his profile)
     *
     * If the user is not logged in then the default network will be selected.
     */
    private void initializeNewsNetwork() {
        if (DataHelper.isNull(newsSearchDTO.getNetwork())) {
            //If no Network is set we have to see which network applies for this user
            this.newsSearchDTO.setNetwork(obtainCurrentUserNetwork());
        }
    }

    public CommentsComp getCommentComp() {
        return commentComp;
    }

    public void setCommentComp(CommentsComp commentComp) {
        this.commentComp = commentComp;
    }

    //======= News Actions - Start These methods should be called from the newsActions2.xhtml =======
    public String activateNews() {
        String answer = Navegation.stayInSamePage;

        try {
            currentNews = this.newsService.activateNews(currentNews);
            this.addFacesMsgFromProperties("news_control_info_news_activated");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }

    public String deleteNews() {
        String answer = Navegation.stayInSamePage;

        try {
            this.newsService.deleteNews(currentNews);
            this.addFacesMsgFromProperties("news_control_info_news_deleted");
        } catch (Exception ex) {
            this.manageException(ex);
        }

        return answer;
    }
    //======= News Actions - End ========================================================
}
