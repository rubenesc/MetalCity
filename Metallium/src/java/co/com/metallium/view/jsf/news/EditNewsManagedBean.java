/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.news;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.Network;
import co.com.metallium.core.entity.News;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.exception.NewsException;
import co.com.metallium.core.service.ImageGalleryService;
import co.com.metallium.view.util.JsfUtil;
import co.com.metallium.view.util.JsfViewReturnPage;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.utils.UtilHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Ruben
 */
@ManagedBean
@ViewScoped
public class EditNewsManagedBean extends NewsBaseManagedBean implements Serializable {

    private Integer id;
    private String view; //This is just an aux variable to store from what page the request is coming to edit the news. This way we know where to send the user when he saves or cancels a request.
    private News news = new News();
    private boolean newsExists = false; //Tells me if the news Im searching for can be displayed. It could exist, but maybe I cant display it, becuase Its pending or something.
    private DualListModel<Network> networks = null;
    @EJB
    ImageGalleryService imageGalleryService;
    private boolean editCoverImage = false; //Tells me if the user editing the a "News" object uploaded a new image or not. I need to know in order to also update the image in the DB when he updates the news.
    private Integer newsId; //This is an aux variable to select an existing news id from whom we want to have there same cover image.

    /** Creates a new instance of PickListManagedBean */
    public EditNewsManagedBean() {

        initializeNetworks();

    }

    /**
     * This method just initializes the networks available for a network. If Im just going
     * to create a news then the Source is all the available networks and the Target is left
     * empty because it shouldn't have any networks because we don't know yet if we are editing
     * or creating an existing news.
     */
    private void initializeNetworks() {
        //
        /**
         * Ok remember some things about working with this Picklist component of prime faces.
         * First here is where I initialize it, even if it is for creating a news or editing a news (this requires one more step).
         *
         * Since I am working with an object 'Network' in the pick list then I have to user a CONVERTER to handle the 'Network' object.
         * 
         * This Network converter has to implement the two methods:
         * public Object getAsObject --> obviously receives the objects Id this case the Integer, and returns the object itself in this case a 'Network' object
         * public String getAsString --> you have to make the case that is going to receive a 'Network' object and you have to return the Id, in this case the Integer
         *
         * Maybe this sounds simple but I had a fucking head ache trying to figure this out!!!  If not, this component will work out strangely.
         *
         */
        List<Network> source = new ArrayList<Network>();
        List<Network> target = new ArrayList<Network>();

        //This is inefficient but well ... fuck it ...
        HashMap<Integer, Network> networkMap = applicationParameters.getNetworkMap();

        Iterator<Integer> iterator = networkMap.keySet().iterator();
        Integer key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            source.add(networkMap.get(key));
        }

        networks = new DualListModel<Network>(source, target);

    }

    /**
     * This method initializes the Networks that the News Has. It is meant to load
     * the networks of a news that is going to be edited. In other words a news that already exists.
     *
     * I also assume that if the news already exists the method 'initializeNetworks()' was called previously
     * so now I can call this method and leave the corresponding networks in the source and target as is required.
     *
     * @param networkCollection = Receives the networks of the news that we are going to edit.
     */
    private void loadNewsNetworks(Collection<Network> networkCollection) {

        List<Network> source = networks.getSource();

        for (Iterator<Network> it = networkCollection.iterator(); it.hasNext();) {
            Network network = it.next();
            source.remove(network);
        }
        networks = new DualListModel<Network>(source, (List<Network>) networkCollection);

    }

    /**
     * This set Id is very Important, its the point of entry of the page.
     * it tells us which news we are going to find to display and edit
     *
     * @param profileId
     */
    public void setId(Integer id) {
        //I set the profile who's Id I get in the request.
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    /**
     * This set Id, should come as a parameter to the page. It actually indicates
     * to what page should you redirect the user when the request is completed.
     * Example: in = index
     *          ncr = news control room
     *
     * @param view
     */
    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    /***
     * This method is called by Pretty Faces.
     * It returns a navigation command to indicate a new page to take the user
     * if the information could not be loaded correctly
     *
     * @return String Navigation Page.
     */
    public String findNews() {

        String answer = Navegation.PRETTY_NEWS_CONTROL_ROOM; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.

        if (id != null) {

            if (!newsExists) { //I make this validation so it only enters the ONE time! Once it finds the news then it wont enter again.
                News newsToFind = null;
                try {
                    newsToFind = newsService.findNewsById(id);
                    loadNewsNetworks(newsToFind.getNetworkCollection());
                    this.setNewsExists(true);

                    answer = Navegation.PRETTY_OK; //The news was loaded correctly so I allow the user to go this news page.

                } catch (Exception ex) {
                    this.setNewsExists(false);
                    this.manageException(ex);
                }
                this.setNews(newsToFind);

            }
        }

        return answer;

    }

    public String performSearchNews() {

        if (this.newsId == null) {
            this.addFacesMsgFromProperties("search_info_validate_write_something");
        }

        if (isJsfMessageListEmpty()) {
            try {
                News auxNews = this.newsService.findNewsById(this.newsId);
                this.getNews().setCoverImage(auxNews.getCoverImage());
                this.getNews().setCoverImageThumbnail(auxNews.getCoverImageThumbnail());
                this.editCoverImage = false;
            } catch (Exception ex) {
                this.manageException(ex);
            }
        }

        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {

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

                PairDTO pairDTO = this.imageGalleryService.uploadImageNews(
                        fileName, contentType, fileSize, uploadedFile.getInputstream());

                this.getNews().setCoverImage(pairDTO.getOne());
                this.getNews().setCoverImageThumbnail(pairDTO.getTwo());
                this.editCoverImage = true;

                this.addFacesMsgFromProperties("image_gallery_info_uploaded_successfully", fileName);
            } catch (GalleryException ex) {
                this.manageException(ex);
            } catch (Exception e) {
                String x = applicationParameters.getResourceBundleMessage("common_info_file_upload_failed");
                this.manageException(new GalleryException(e.getMessage(), e, x));
            }


        } else {
            this.addFacesMsgFromProperties("common_info_restricted_access");
        }
    }

    public String createNews() {
        view = JsfViewReturnPage.VIEW_NEWS_CONTROL_ROOM; //Whenever I create a News Im sending him back to the Control Room.
        return executeNewsUpdate(true);
    }

    public String editNews() {
        return executeNewsUpdate(false);
    }

    public String cancel() {
        return JsfViewReturnPage.getPage(view);
    }

    private String executeNewsUpdate(boolean isCreate) {

        String answer = JsfViewReturnPage.getPage(view);
        this.validateNewsUpdate();

        if (JsfUtil.getMessageListSize() == 0) {
            try {

                if (this.isAuthenticated()) {
                    this.news.setUser(new User(this.getAuthenticaedUser().getId()));

                    if (isCreate) {

                        if (networks.getTarget().size() > 0) {
                            news.setNetworkCollection(networks.getTarget());
                        }

                        newsService.createNews(news, editCoverImage);
                        this.addFacesMsgFromProperties("news_control_info_news_created");
                    } else {
                        news.setNetworkCollection(networks.getTarget());
                        newsService.editNews(news, editCoverImage);
                        this.addFacesMsgFromProperties("common_info_save_success");
                    }

                } else {
                    throw new NewsException("", null, applicationParameters.getResourceBundleMessage("common_info_user_must_be_authenticated"));
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

    private void validateNewsUpdate() {

        if (UtilHelper.isStringEmpty(this.getNews().getCoverImage())) {
            this.addFacesMsgFromProperties("news_info_cover_image_required");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getNews().getTitle())) {
            this.addFacesMsgFromProperties("news_field_title");
            return;
        }

        if (UtilHelper.isStringEmpty(this.getNews().getContent())) {
            this.addFacesMsgFromProperties("news_field_content");
            return;
        }



    }

    //============Getters and Setters=======================================//
    public DualListModel<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(DualListModel<Network> networks) {
        this.networks = networks;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public boolean isNewsExists() {
        return newsExists;
    }

    public void setNewsExists(boolean newsExists) {
        this.newsExists = newsExists;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

}
