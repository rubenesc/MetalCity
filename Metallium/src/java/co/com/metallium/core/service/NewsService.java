/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.NewsState;
import co.com.metallium.core.constants.state.UserProfile;
import co.com.metallium.core.entity.News;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.exception.NewsException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ruben
 */
@Stateless
public class NewsService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    private @EJB
    UserService userService;
    @EJB
    ImageGalleryService imageGalleryService;
    @EJB
    private GeneralService generalService;

    public NewsService() {
    }

    /**
     * Creates a new News article.
     *
     * @param event = contains the event to create with the information to persist.
     * @param uploadImage = if TRUE then it means we uploaded a new image so we have to process it (copy from temp dir to the new event dir)
     *                      if FALSE then it means we copied an image from another NEWS so we don't have to process it.
     * @return
     * @throws EventException
     */
    public News createNews(News news, boolean uploadImage) throws NewsException, ValidationException {

        User userEditing = null;
        try {
            userEditing = this.userService.findActiveUserById(news.getUser().getId());
        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }

        news.setContent(HtmlSanitizer.admin(news.getContent())); //Clean HTML input.
        validateNewsEventLength(news.getContent());

        try {

            Integer profile = userEditing.getProfile();

            if (UserProfile.isAdministrator1(profile)) {

                if (uploadImage) {
                    //It means that I uploaded an image so I have to process it.
                    //If it doesnt enter here then it means that I copied the image from another event so that image location already comes in the News entity.
                    PairDTO pairDTO = imageGalleryService.processNewsUploadedImages(news.getCoverImage());
                    news.setCoverImage(pairDTO.getOne());
                    news.setCoverImageThumbnail(pairDTO.getTwo());
                }

                String firstParagraph = DataHelper.getNewsPreviewString(news.getContent());
                news.setPreview(firstParagraph);
                news.setState(NewsState.PENDING);
                news.setDate(new Date());
                news.setNumberComments(0);

                String titleAlias = this.generalService.generateAliasForTitle(news.getTitle(), News.namedQueryCountAlias, true, null);
                news.setAlias(titleAlias);
                em.persist(news);
            } else {
                throw new NewsException("User with id: " + userEditing.getId() + ", and Profile: " + userEditing.getProfile() + ", cant Create News!!!", null, applicationParameters.getResourceBundleMessage("common_info_fuck_you"));
            }

        } catch (NewsException e) {
            throw e;
        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }


        return news;
    }

    public News editNews(News news, boolean editCoverImage) throws NewsException, ValidationException {

        News newsToEdit = this.findNewsById(news.getId());

        User userEditing = null;
        try {
            userEditing = this.userService.findActiveUserById(news.getUser().getId());
        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }

        news.setContent(HtmlSanitizer.admin(news.getContent())); //Clean HTML input.
        validateNewsEventLength(news.getContent());

        try {
            Integer profile = userEditing.getProfile();

            if (UserProfile.isAdministrator1(profile)) {

                if (editCoverImage) {
                    //This means that I uploaded a new image so I have to process it from a temp dir to the apropiate news dir.
                    PairDTO pairDTO = imageGalleryService.processOverwriteUploadedImage(news.getCoverImage(), newsToEdit.getCoverImage());
                    newsToEdit.setCoverImage(pairDTO.getOne());
                    newsToEdit.setCoverImageThumbnail(pairDTO.getTwo());
                } else {
                    //This means that either the image wasn't edited or that we are going to copy 
                    //the image from another news.
                    newsToEdit.setCoverImage(news.getCoverImage());
                    newsToEdit.setCoverImageThumbnail(news.getCoverImageThumbnail());
                }

                if (news.getNetworkCollection() == null || news.getNetworkCollection().isEmpty()) {
                    newsToEdit.setNetworkCollection(null);
                    newsToEdit.setState(NewsState.PENDING);//If there are no networks associated with the news I set the news as Pending
                } else {
                    newsToEdit.setNetworkCollection(news.getNetworkCollection());
                    newsToEdit.setState(news.getState()); //If there is one or more networks associated with the news I set the state, normally
                }

                String firstParagraph = DataHelper.getNewsPreviewString(news.getContent());
                String firstParagraphWithNoImages = HtmlSanitizer.admin(firstParagraph);  //Clean HTML input.
                newsToEdit.setPreview(firstParagraphWithNoImages);
                newsToEdit.setMediaUrl(news.getMediaUrl());
                newsToEdit.setSourceUrl(news.getSourceUrl());

                if (UtilHelper.areStringsNotEqual(newsToEdit.getTitle(), news.getTitle().trim())) {
                    //Only if the Title was modified do I set the new title and generate
                    //a new alias.
                    newsToEdit.setTitle(news.getTitle().trim());

                    String titleAlias = this.generalService.generateAliasForTitle(news.getTitle(), News.namedQueryCountAlias, false, newsToEdit.getId());
                    newsToEdit.setAlias(titleAlias);
                }

                newsToEdit.setContent(news.getContent());
                newsToEdit.setUser(userEditing);
                em.merge(newsToEdit);

            } else {
                throw new NewsException("User with id: " + userEditing.getId() + ", and Profile: " + userEditing.getProfile() + ", cant Edit News!!!", null, applicationParameters.getResourceBundleMessage("common_info_fuck_you"));
            }

        } catch (NewsException e) {
            throw e;
        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }

        return news;
    }

    public void deleteNews(News news) throws NewsException {

        try {

            if (NewsState.isActive(news.getState()) || NewsState.isPending(news.getState())) {
                news.setState(NewsState.DELETED);
                em.merge(news);
            } else {
                LogHelper.makeLog("... Cant delete news because it's state its: " + news.getState());
            }

        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }

    }

    public News activateNews(News news) throws NewsException {

        News newsToActivate = this.findNewsById(news.getId());
        if (newsToActivate.getNetworkCollection() == null || newsToActivate.getNetworkCollection().isEmpty()) {
            String msg = applicationParameters.getResourceBundleMessage("news_control_info_network_required");
            throw new NewsException(msg, null, msg);
        }

        try {

            if (NewsState.isDeleted(news.getState())
                    || NewsState.isInactive(news.getState())
                    || NewsState.isPending(news.getState())) {
                newsToActivate.setState(NewsState.ACTIVE);
                em.merge(newsToActivate);
            } else {
                LogHelper.makeLog("... Cant activate news because it's state is: " + news.getState());
            }

        } catch (Exception e) {
            throw new NewsException(e.getMessage(), e);
        }
        return newsToActivate;

    }

    public void edit(News news) {
        em.merge(news);
    }

    public void remove(News news) {
        try {
            em.remove(em.merge(news));
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
    }

    public News findNewsByAlias(String alias) throws NewsException {
        News news = null;

        try {
            news = (News) em.createNamedQuery("News.findByAlias").setParameter("alias", alias).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The news with alias: " + alias + ", could not be found in the system.";
                throw new NewsException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new NewsException(ex.getMessage(), ex);
            }
        }
        return news;
    }

    public News findNewsById(Integer id) throws NewsException {
        News news = null;

        try {
            news = (News) em.createNamedQuery("News.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The news with id: " + id + ", could not be found in the system.";
                throw new NewsException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new NewsException(ex.getMessage(), ex);
            }
        }
        return news;
    }

    public void updateNumberComments(Integer newsId, int numberOfComments) throws NewsException {
        News newsToUpdate = this.findNewsById(newsId);
        newsToUpdate.setNumberComments(numberOfComments);
        this.edit(newsToUpdate);
    }
}
