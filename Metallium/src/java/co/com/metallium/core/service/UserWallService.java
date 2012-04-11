/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.UserWallPostTypeEnum;
import co.com.metallium.core.entity.UserWallPost;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.exception.UserWallException;
import co.com.metallium.core.exception.ValidationException;
import co.com.metallium.core.util.Assert;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;

/**
 * 20110815
 * @author Ruben
 */
@Stateless
public class UserWallService extends BaseService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    ImageGalleryService imageGalleryService;

    public UserWallService() {
    }

    public UserWallPost findPostById(Integer id) throws UserWallException {
        UserWallPost answer = null;

        try {
            answer = (UserWallPost) em.createNamedQuery("UserWallPost.findById").setParameter("id", id).getSingleResult();

        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The post with id: " + id + ", could not be found in the system.";
                throw new UserWallException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new UserWallException(ex.getMessage(), ex);
            }
        }
        return answer;
    }

    public UserWallPost findPostByPostIdAndUserId(Integer id, Integer userId) throws UserWallException {
        UserWallPost answer = null;

        try {
            answer = (UserWallPost) em.createNamedQuery("UserWallPost.findByPostIdAndUserId").setParameter("id", id).setParameter("userId", userId).getSingleResult();

        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The post with id: " + id + ", could not be found in the system.";
                throw new UserWallException(msg, ex, applicationParameters.getResourceBundleMessage("search_info_no_results"));
            } else {
                throw new UserWallException(ex.getMessage(), ex);
            }
        }
        return answer;
    }

    public void createPost(UserWallPost post) throws UserWallException, ValidationException {

//        validateIfUserCanCreateComment(comment.getUserWallId(), comment.getUser().getId());


        if (post.getType() == null) {
            post.setType(UserWallPostTypeEnum.TEXT);
        }

        UserWallPost entityToPersist = new UserWallPost();

        entityToPersist.setDate(new Date());
        entityToPersist.setType(post.getType());
        entityToPersist.setUserId(post.getUserId());

        entityToPersist.setContent(HtmlSanitizer.wallPost(post.getContent())); //Clean HTML input.. Sanitize evil shit!!!
        validateWallPostLength(entityToPersist.getContent());


        try {
            if (UserWallPostTypeEnum.LINK.equals(post.getType())) {

                String link = validateWallLink(post.getUrl()); //Sanitize evil shit!!!
                entityToPersist.setUrl(link);

            } else if (UserWallPostTypeEnum.IMAGE.equals(post.getType())) {
                PairDTO pairDTO = imageGalleryService.processWallUploadedImages(post.getImage(), post.getUserId());
                entityToPersist.setImage(pairDTO.getOne());
                entityToPersist.setImageThumbnail(pairDTO.getTwo());
            } else if (UserWallPostTypeEnum.VIDEO.equals(post.getType())) {
                String videoId = getYouTubeIdWrapper(post.getUrl());

                if (videoId != null) {
                    processMediaInfo(videoId, entityToPersist);
                }

                entityToPersist.setUrl(HtmlSanitizer.link(post.getUrl())); //Clean HTML input.. Sanitize evil shit!!!
                entityToPersist.setMediaId(videoId);
            }


            em.persist(entityToPersist);
        } catch (UserWallException e) {
            throw e;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new UserWallException(e.getMessage(), e);
        }

    }

    public UserWallPost editPost(UserWallPost post, boolean editImage) throws UserWallException, ValidationException {

        UserWallPost entityToEdit = this.findPostById(post.getId());

        String coverImageUrl = null;
        String coverImageThumbnailUrl = null;
        boolean deleteCurrentImage = false;


        UserWallPostTypeEnum actualType = entityToEdit.getType();
        UserWallPostTypeEnum newType = post.getType();

        entityToEdit.setType(post.getType());
        entityToEdit.setContent(HtmlSanitizer.wallPost(post.getContent())); //Clean HTML input.. Sanitize evil shit!!!
        validateWallPostLength(entityToEdit.getContent());

        try {


            if (UserWallPostTypeEnum.IMAGE.equals(actualType)) {
                coverImageUrl = entityToEdit.getImage();
                coverImageThumbnailUrl = entityToEdit.getImageThumbnail();
            }

            if (editImage && UserWallPostTypeEnum.IMAGE.equals(newType)) {

                PairDTO pairDTO = null;
                if (StringUtils.isNotBlank(entityToEdit.getImage())) {
                    pairDTO = imageGalleryService.processOverwriteUploadedImage(post.getImage(), entityToEdit.getImage());
                } else {
                    pairDTO = imageGalleryService.processWallUploadedImages(post.getImage(), post.getUserId());
                }

                entityToEdit.setImage(pairDTO.getOne());
                entityToEdit.setImageThumbnail(pairDTO.getTwo());
                //I do not delete an image if I am editing it. It will be overriden.
            }


            //If I had an Image and Im creating something other than an Image
            //then I have to delete the current image.
            if (UserWallPostTypeEnum.IMAGE.equals(actualType)
                    && !UserWallPostTypeEnum.IMAGE.equals(newType)) {
                deleteCurrentImage = true;
            }

            if (UserWallPostTypeEnum.TEXT.equals(post.getType())) {
                entityToEdit.setUrl(null);
                entityToEdit.setMediaId(null);
                entityToEdit.setImage(null);
                entityToEdit.setImageThumbnail(null);
            } else if (UserWallPostTypeEnum.LINK.equals(post.getType())) {
                entityToEdit.setImage(null);
                entityToEdit.setImageThumbnail(null);

                String link = validateWallLink(post.getUrl());
                entityToEdit.setUrl(link);

                entityToEdit.setMediaId(null);
            } else if (UserWallPostTypeEnum.IMAGE.equals(post.getType())) {
                entityToEdit.setUrl(null);
                entityToEdit.setMediaId(null);
            } else if (UserWallPostTypeEnum.VIDEO.equals(post.getType())) {
                entityToEdit.setImage(null);
                entityToEdit.setImageThumbnail(null);
                String videoId = getYouTubeIdWrapper(post.getUrl());

                if (videoId != null) {
                    processMediaInfo(videoId, entityToEdit);
                }

                entityToEdit.setUrl(HtmlSanitizer.link(post.getUrl())); //Clean HTML input.. Sanitize evil shit!!!
                entityToEdit.setMediaId(videoId);
            }

            entityToEdit.setModified(new Date());

            em.merge(entityToEdit);
            em.flush();

        } catch (UserWallException ex) {
            throw ex;
        } catch (ValidationException ex) {
            throw ex;
        } catch (Exception e) {
            throw new UserWallException(e.getMessage(), e);
        }

        if (deleteCurrentImage) {
            try {
                imageGalleryService.deleteImage(coverImageUrl, coverImageThumbnailUrl);
            } catch (GalleryException ex) {
                LogHelper.makeLog(ex);
            }
        }


        return post;
    }

    /**
     * 
     * Validate method to verify that the link that is going to be published on 
     * the wall is at least a little valid or not. There are alot of more validations
     * to be done, but for now we are only doing some basic ones. 
     * 
     * Examples of different validations can be seen here: http://ha.ckers.org/xss.html
     * 
     * @param post
     * @return
     * @throws UserWallException 
     */
    private String validateWallLink(String link) throws UserWallException {
        link = HtmlSanitizer.link(link); //Clean HTML input.. Sanitize evil shit!!!
        try {
            link = Assert.validateLink(link);
        } catch (Exception ex) {
            String msg = applicationParameters.getResourceBundleMessage("link_invalid", link);
            throw new UserWallException(null, null, msg);
        }
        return link;
    }

    public void deletePost(UserWallPost entity) throws UserWallException {

        UserWallPost entityToEdit = this.findPostById(entity.getId());

        try {

            if (UtilHelper.isStringNotEmpty(entity.getImage())) {
                //Then it has an image which I must delete
                imageGalleryService.deleteImage(entity.getImage(), entity.getImageThumbnail());
            }

            em.remove(entityToEdit);
            em.flush();
        } catch (Exception e) {
            throw new UserWallException(e.getMessage(), e);
        }

    }

}
