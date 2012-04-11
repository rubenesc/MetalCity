/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.ImagesConst;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.constants.state.GalleryState;
import co.com.metallium.core.service.dto.ImageDTO;
import co.com.metallium.core.entity.ImageGallery;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.GalleryException;
import co.com.metallium.core.service.dto.search.ImageGallerySearchDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.image.ImageHelper;
import com.metallium.utils.utils.FileUtil;
import com.metallium.utils.utils.ImageExtentionFileFilter;
import com.metallium.utils.utils.UtilHelper;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20101124
 * @author Ruben
 */
@Stateless
public class ImageGalleryService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @EJB
    private UserService userService;
    @EJB
    private GeneralService generalService;
    @EJB
    private ApplicationParameters applicationParameters;

    public ImageGalleryService() {
    }

    public void deleteUserProfileImage(Integer userId) throws GalleryException {

        String absoluteGalleryDir = null;
        try {
            //  E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + profile
            String galleryDir = UserCommon.FILE_SYSTEM_USER_PROFILE;
            absoluteGalleryDir = MetUtilHelper.getAbsoluteUserPath(userId).concat(galleryDir);
            this.generalService.deleteAllImagesInFolderFromFileSystem(absoluteGalleryDir); //I wipe all the files there first.
        } catch (Exception e) {
            throw new GalleryException("Error trying to wipe out the files in --> " + absoluteGalleryDir + ", ex: " + e.getMessage(), e);
        }
    }

    public short deleteImageFromGallery(Integer galleryId, String fileName) throws GalleryException {

        short numberOfFiles = 0;
        ImageGallery imageGallery = null;

        Integer userId = null;
        String galleryDir = null;

        try {
            imageGallery = this.findImageGalleryById(galleryId);
            userId = imageGallery.getUser().getId();
            galleryDir = imageGallery.getDirectory();

            if (!MetUtilHelper.isDirectoyAGallery(galleryDir)) {
                String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_image_can_not_be_deleted", fileName);
                throw new GalleryException(msg, null, msg);
            }

            if (!MetUtilHelper.isFileAnImage(fileName)) {
                String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_file_is_not_an_image", fileName);
                throw new GalleryException(msg, null, msg);
            }

            this.generalService.deleteImageFromFileSystem(userId, galleryDir, fileName);

        } catch (GalleryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }


        //and now that I deleted the files, well I just simply update the number of files in the gallery,
        //and I will also verify the cover image of the gallery

        try {

            String absoluteGalleryDir = MetUtilHelper.getAbsoluteUserPath(userId).concat(galleryDir);
            numberOfFiles = (short) MetUtilHelper.countImageFilesInDirectory(absoluteGalleryDir);

            boolean setDefaultCoverImage = false;
            try {

                if (numberOfFiles == 0) {
                    setDefaultCoverImage = true;
                }

                //I check to see if the image Im deleting is the cover image of the gallery
                if (!setDefaultCoverImage && fileName.equalsIgnoreCase(FileUtil.trimFilePath(imageGallery.getCover()))) {
                    //It is the cover image so I have to set anotheone
                    setDefaultCoverImage = true;
                }

            } catch (Exception e) {
                LogHelper.makeLog(e);
                setDefaultCoverImage = true;
            }

            if (setDefaultCoverImage) {
                //I set the default image gallery cover.
                imageGallery.setCover(MetConfiguration.DEFAULT_IMAGE_GALLERY_COVER);
            }

            //I also update the number of files in the gallery
            imageGallery.setNumberPics(numberOfFiles);
            em.merge(imageGallery);
        } catch (Exception e) {
            //This doesn't really afect anything
            LogHelper.makeLog(e);
        }

        return numberOfFiles;
    }

    public void deleteImageGallery(ImageGallery gallery) throws GalleryException {

        ImageGallery entity = this.findImageGalleryById(gallery.getId());

        try {
            String directory = entity.getDirectory();
            if (!MetUtilHelper.isDirectoyAGallery(directory)) {
                String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_can_not_be_deleted", entity.getTitle());
                throw new GalleryException(msg, null, msg);
            }

            this.generalService.deleteGalleryFromFileSystem(entity.getUser().getId(), directory);
            em.remove(entity);
        } catch (GalleryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }

    }

    public void deleteImage(String coverImageUrl, String coverImageThumbnailUrl) throws GalleryException {
        deleteImagesFromFileSystem(coverImageUrl, coverImageThumbnailUrl);
    }

    /**
     * 
     * @param imageUrl1 <-- image relative Path
     * @param imageUrl2 <-- image relative Path
     * @throws GalleryException
     */
    private void deleteImagesFromFileSystem(String imageUrl1, String imageUrl2) throws GalleryException {

        //now we convert a relative path to an aboslute path
        //events/images/event01/image01.jpg    -->  E:/Desarrollo/Dunkelheit/FileSystem/events/images/event01/image01.jpg
        imageUrl1 = MetUtilHelper.convertToAbsolutePathToSomething(imageUrl1);
        imageUrl2 = MetUtilHelper.convertToAbsolutePathToSomething(imageUrl2);

        //TODO - Validate if the images actually are valid directories
        if (!MetUtilHelper.isFileAnImage(imageUrl1)) {
            String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_file_is_not_an_image", imageUrl1);
            throw new GalleryException(msg, null, msg);
        }
        if (!MetUtilHelper.isFileAnImage(imageUrl2)) {
            String msg = applicationParameters.getResourceBundleMessage("image_gallery_info_file_is_not_an_image", imageUrl1);
            throw new GalleryException(msg, null, msg);
        }

        try {
            MetUtilHelper.deleteFile(imageUrl1); //image
            MetUtilHelper.deleteFile(imageUrl2); //thumbnail
        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }
    }

    public String setGalleryCoverImage(Integer galleryId, String imageName) throws GalleryException {
        ImageGallery entity = this.findImageGalleryById(galleryId);
        String coverImagePath = null;
        try {
            coverImagePath = MetUtilHelper.getRelativePathToUserAlbumCover(entity.getUser().getId(), entity.getDirectory(), imageName);
            entity.setCover(coverImagePath);
            em.merge(entity);
        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }
        return coverImagePath;
    }

    public ImageGallery createImageGallery(ImageGallery gallery) throws GalleryException {

        User registerdUser = null;
        try {
            registerdUser = this.userService.findUserById(gallery.getUser().getId());
        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }

        try {
            String galleryDirectory = generalService.createUserFileSystemGallery(registerdUser.getId());
            gallery.setCover(MetConfiguration.DEFAULT_IMAGE_GALLERY_COVER);
            gallery.setState(GalleryState.PRIVATE);
            gallery.setNumberPics(Short.valueOf("0"));
            gallery.setDate(new Date());
            gallery.setUser(registerdUser);
            gallery.setDirectory(galleryDirectory);

            //Seteo datos que por ahora no voy a utilizar
            em.persist(gallery);
            em.flush();
            em.refresh(gallery);

        } catch (Exception ex) {
            throw new GalleryException(ex.getMessage(), ex);
        }


        return gallery;
    }

    public ImageGallery editImageGalleryBasicInfo(ImageGallery gallery) throws GalleryException {
        ImageGallery galleryToEdit = this.findImageGalleryById(gallery.getId());

        try {
            galleryToEdit.setTitle(gallery.getTitle());
            galleryToEdit.setDescription(gallery.getDescription());
            galleryToEdit.setState(gallery.getState());
            em.merge(galleryToEdit);
        } catch (Exception e) {
            throw new GalleryException(e.getMessage(), e);
        }

        return galleryToEdit;
    }

    public ImageGallery findImageGalleryById(int id) throws GalleryException {
        ImageGallery entity = null;

        try {
            entity = (ImageGallery) em.createNamedQuery("ImageGallery.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg1 = "The gallery with id: " + id + ", could not be found in the system.";
                String msg2 = applicationParameters.getResourceBundleMessage("image_gallery_info_gallery_doesnt_exist");
                throw new GalleryException(msg1, ex, msg2);
            } else {
                throw new GalleryException(ex.getMessage(), ex);
            }
        }
        return entity;
    }

    public int countGalleriesWhereUserIsOwner(ImageGallerySearchDTO criteriaDTO) {
        int answer = 0;
        try {

            answer = ((Number) em.createNamedQuery("ImageGallery.countGalleriesWhereUserIsOwner").setParameter("userId", criteriaDTO.getUserId()).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    public List<ImageGallery> findUserGalleriesByRangeWhereUserIsOwner(int[] range, ImageGallerySearchDTO criteriaDTO) {
        List<ImageGallery> answer = null;

        try {
            Query q = em.createNamedQuery("ImageGallery.findGalleriesWhereUserIsOwner");
            q.setParameter("userId", criteriaDTO.getUserId());
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            answer = q.getResultList();
        } catch (Exception e) {
            answer = new ArrayList<ImageGallery>();
            LogHelper.makeLog(e.getMessage(), e);
        }


        return answer;
    }

    public int countGalleriesWhereUserIsAnonymous(ImageGallerySearchDTO criteriaDTO) {
        int answer = 0;
        try {

            answer = ((Number) em.createNamedQuery("ImageGallery.countGalleriesWhereUserIsAnonymous").setParameter("userId", criteriaDTO.getUserId()).setParameter("state", GalleryState.PUBLIC).getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    public List<ImageGallery> findUserGalleriesByRangeWhereUserIsAnonymous(int[] range, ImageGallerySearchDTO criteriaDTO) {
        List<ImageGallery> answer = null;

        try {
            Query q = em.createNamedQuery("ImageGallery.findGalleriesWhereUserIsAnonymous");
            q.setParameter("userId", criteriaDTO.getUserId());
            q.setParameter("state", GalleryState.PUBLIC);
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            answer = q.getResultList();
        } catch (Exception e) {
            answer = new ArrayList<ImageGallery>();
            LogHelper.makeLog(e.getMessage(), e);
        }


        return answer;
    }

    /**
     * Counts the number of images that are in an specific directory
     *
     * @return int number of files
     */
    public int countImageFilesByUserGallery(ImageGallerySearchDTO criteriaDTO) {
        int answer = 0;
        try {
            String directory = MetConfiguration.FILE_SYSTEM + criteriaDTO.getGalleryDirectory();
            answer = MetUtilHelper.countImageFilesInDirectory(directory);
        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }
        return answer;
    }

    /**
     *
     * Finds the names of the files in a specific directory. So it returns of list
     * of these names, but only the names in the desired range. 
     * 
     * it will return a collection of names like 
     *  users/user00/00/00/01/profile/image1.jpg
     *
     */
    public List<ImageDTO> findImageFilesByUserGallery(int[] range, ImageGallerySearchDTO criteriaDTO) {

        List<ImageDTO> answer = new ArrayList<ImageDTO>();
        try {

            //users/user00/00/00/01/profile  + /
            String userPath = criteriaDTO.getGalleryDirectory().concat("/");
            //E:/Desarrollo/Dunkelheit/FileSystem/ +  users/user00/00/00/01/profile/"
            File file = new File(MetConfiguration.FILE_SYSTEM + userPath);

            if (file.isDirectory()) {

                //Im only intrested in Listing all the files that have an image extention. Thats why
                //I apply the Filter, if not it would return even dirctories or what ever.
                File[] files = file.listFiles(ImageExtentionFileFilter.getInstance());

                int maxResults = range[1] - range[0];
                int firstResult = range[0];
                int count = files.length;

                if (maxResults > count) {
                    maxResults = count;
                }
                int lastResult = firstResult + maxResults;
//                LogHelper.makeLog("Range[0] = " + range[0] + ", Range[1] = " + range[1]);
//                LogHelper.makeLog("firstResult = " + firstResult + ", lastResult = " + lastResult);

                packImages(files, userPath, answer, firstResult, lastResult);

            } else {
                LogHelper.makeLog("This isn't supposed to happen, this is should be a directory: " + userPath + " for user with id: " + criteriaDTO.getUserId());
            }


        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    /**
     * This one just finds all the images in the directory without worrying about the
     * range, Its not to worry because there shouldn't be that many to cause a problem.
     *
     *
     */
    public List<ImageDTO> findImageFilesByUserGallery(ImageGallerySearchDTO criteriaDTO) {

        List<ImageDTO> answer = new ArrayList<ImageDTO>();
        try {

            //users/user00/00/00/01/profile  + /
            String userPath = criteriaDTO.getGalleryDirectory().concat(MetConfiguration.fileSystemSeparator);
            //E:/Desarrollo/Dunkelheit/FileSystem/ +  users/user00/00/00/01/profile/"
            File file = new File(MetConfiguration.FILE_SYSTEM.concat(userPath));

            if (file.isDirectory()) {

                //Im only intrested in Listing all the files that have an image extention. Thats why
                //I apply the Filter, if not it would return even dirctories or what ever.
                File[] files = file.listFiles(ImageExtentionFileFilter.getInstance());
                packImages(files, userPath, answer, 0, files.length);

            } else {
                LogHelper.makeLog("This isn't supposed to happen, this is should be a directory: " + userPath + " for user with id: " + criteriaDTO.getUserId());
            }

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    /**
     *
     * Method that given an File[] returns a List<ImageDTO>
     * which contains the files in a format I can display in a web page.
     *
     * @param files = contains the image files
     * @param userPath = the user path where the images are stored
     * @param answer = is the list where Im going to pack the images in
     * @param start = from where I start looking in the files array
     * @param end = from where I stop looking in the files array
     * @return
     */
    private List<ImageDTO> packImages(File[] files, String userPath, List<ImageDTO> answer, int start, int end) {
        for (int i = start; i < end; i++) {
            try {
                userPath = MetUtilHelper.removeBackSlashesFromFilePath(userPath);
                String imageURL = userPath.concat(files[i].getName());
                String thumbnailURL = MetUtilHelper.getThumbnailLocation(userPath, files[i].getName());
                answer.add(new ImageDTO(imageURL, thumbnailURL, files[i].getName()));
            } catch (Exception e) {
                LogHelper.makeLog(e);
            }
        }
        return answer;
    }

    public PairDTO uploadProfileImage(Integer userId, String fileName, String contentType, long fileSize, InputStream inputStream) throws GalleryException, Exception {

        validateImageInput(fileName, fileSize);

        // Precreate an unique file and then write the InputStream of the uploaded file to it.
        String uploadedFileName = FileUtil.trimFilePath(fileName); // "image.jpg"
        String sPicTemp = MetUtilHelper.getNewFileNameKeepExtention(uploadedFileName, UserCommon.PROFILE_PIC_TEMP); //profileTemp.jpg
        String sPicL = MetUtilHelper.getNewFileNameKeepExtention(uploadedFileName, UserCommon.PROFILE_PIC_LARGE); //profileL.jpg
        String sPicM = MetUtilHelper.getNewFileNameKeepExtention(uploadedFileName, UserCommon.PROFILE_PIC_MED); //profileM.jpg
        String sPicS = MetUtilHelper.getNewFileNameKeepExtention(uploadedFileName, UserCommon.PROFILE_PIC_SMALL); //profileS.jpg

        //  E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + profile
        String galleryDir = UserCommon.FILE_SYSTEM_USER_PROFILE;
        String relativeGalleryDir = MetUtilHelper.getRelativeUserPath(userId).concat(galleryDir); //I'll use this later on.
        String absoluteGalleryDir = MetUtilHelper.getAbsoluteUserPath(userId).concat(galleryDir);
        this.generalService.deleteAllImagesInFolderFromFileSystem(absoluteGalleryDir); //I wipe all the files there first.

        File picTemp = FileUtil.uniqueFile(new File(absoluteGalleryDir), sPicTemp);
        File picL = FileUtil.uniqueFile(new File(absoluteGalleryDir), sPicL);
        File picM = FileUtil.uniqueFile(new File(absoluteGalleryDir), sPicM);
        File picS = FileUtil.uniqueFile(new File(absoluteGalleryDir), sPicS);

        FileUtil.write(picTemp, inputStream);


        //Ok when I get here is because I have already figured out, where Im going to put the images
        //im going to process. Now Im going to scale them to there desired size and location.
        ImageHelper.processImageDimensions(picTemp, picL, MetConfiguration.IMAGE_MAX_WIDTH_SIZE, false); //The image stays in the same place, Im just going to make it smaller.
        ImageHelper.processImageDimensions(picL, picM, MetConfiguration.IMAGE_PROFILE_MAX_WIDTH_SIZE, false); //The image stays in the same place, Im just going to make it smaller.
        ImageHelper.processImageDimensions(picM, picS, MetConfiguration.IMAGE_PROFILE_THUMBNAIL_MAX_WIDTH_SIZE, true); //The image stays in the same place, Im just going to make it smaller.
        picTemp.delete(); //Finaly I delete the Temp file.


        PairDTO answer = new PairDTO();
        //I finaly return  User00/00/00/01/profile/profileS.jpg
        answer.setOne(MetUtilHelper.removeBackSlashesFromFilePath(relativeGalleryDir.concat(MetConfiguration.fileSystemSeparator).concat(picS.getName())));
        //                 User00/00/00/01/profile/profileM.jpg
        answer.setTwo(MetUtilHelper.removeBackSlashesFromFilePath(relativeGalleryDir.concat(MetConfiguration.fileSystemSeparator).concat(picM.getName())));

        return answer;
    }

    /**
     * @param currentImageLocation = "events/images/temp/20101228163802pOI5.jpg"
     * @param newImageLocation = "events/images/events01/
     * @return
     * @throws Exception
     */
    public PairDTO processEventUploadedImages(String currentImageLocation) throws Exception {
        String dir = MetUtilHelper.getUploadImageDir(MetConfiguration.EVENTS_IMAGE_DIR, "event", null);
        return processUploadedImages(currentImageLocation, dir, false);
    }

    /**
     * @param currentImageLocation = "news/images/temp/20101228163802pOI5.jpg"
     * @param newImageLocation = "news/images/news01/
     * @return
     * @throws Exception
     */
    public PairDTO processNewsUploadedImages(String currentImageLocation) throws Exception {
        String dir = MetUtilHelper.getUploadImageDir(MetConfiguration.NEWS_IMAGE_DIR, "news", null);
        return processUploadedImages(currentImageLocation, dir, false);
    }

    /**
     * @param currentImageLocation = temp file location
     * @param newImageLocation = new dir
     * @return
     * @throws Exception
     */
    public PairDTO processWallUploadedImages(String currentImageLocation, Integer userId) throws Exception {
        //currentImageLocation = "news/images/temp/20101228163802pOI5.jpg";
        String dir = MetUtilHelper.getUploadImageDir(UserCommon.FILE_SYSTEM_USER_WALL, "images", userId);     //Now we have the final Dir where we are going to put the images
        return processUploadedImages(currentImageLocation, dir, false);
    }

    /**
     * @param currentImageLocation = "news/images/temp/20101228163802pOI5.jpg"
     * @param newImageLocation = "news/images/news01/20101228163802pOI5.jpg"
     * @return
     * @throws Exception
     */
    public PairDTO processOverwriteUploadedImage(String currentImageLocation, String newImageLocation) throws Exception {
        return processUploadedImages(currentImageLocation, newImageLocation, true);
    }

    /**
     * Im going to copy and delete an image from one directory to another directory
     * and this also includes the images thumbnail.
     *
     * What happens is that in the news section, you have to upload a cover image,
     * so it is first uploaded into a temp folder. Once the user decides to either
     * create the news or edit it, then, we take this image what is in a tempo folder
     * and copy it to a definite folder where it will be. This also includes the images
     * thumbnail.
     *
     * This temp folder where it is first stored is deleted every once in a while, things
     * left here are garbage.
     *
     * At the end I just return a DTO with the two new file locations.
     *

     * Remember I return PairDTO, where
     *  one = image location
     *  two = thumbnail location
     *
     * @param currentImageLocation ---> Location of the current image on the server
     * @param newImageLocation ---> where the image is going to be located. This can be either a directory or a complete file location.
     * @param overwrite --> true if the newImageLocation is a complete file location. false if it is a directory
     *
     * @return
     * @throws Exception
     */
    private PairDTO processUploadedImages(String currentImageLocation, String newImageLocation, Boolean overwrite) throws Exception {


        PairDTO currentDTO = FileUtil.trimFilePathExt(currentImageLocation);
        String imageDir = currentDTO.getOne(); // news/images/temp/
        String imageName = currentDTO.getTwo(); //20101228163802pOI5.jpg


        PairDTO currentDTO2 = null;
        String imageDir2 = null;
        String imageName2 = null;

        if (overwrite) {
            currentDTO2 = FileUtil.trimFilePathExt(newImageLocation);
            imageDir2 = currentDTO2.getOne(); // news/images/temp/
            imageName2 = currentDTO2.getTwo(); //20101228163802pOI5.jpg
        }


        // news/images/temp/thumbnails/20101228163802pOI5.jpg
        String dirThumbnail = MetUtilHelper.getThumbnailLocation(imageDir, imageName);

        String newDirThumbnail = null;
        if (overwrite) {
            newDirThumbnail = MetUtilHelper.getThumbnailLocation(imageDir2, imageName2);
        } else {
            // news/images/news01/thumbnails/20101228163802pOI5.jpg
            newDirThumbnail = MetUtilHelper.getThumbnailLocation(newImageLocation, imageName);
        }

        // E://bla bla bla + /news/images/temp/20101228163802pOI5.jpg
        File imageFileCurrentLoc = new File(MetUtilHelper.convertToAbsolutePathToSomething(imageDir), imageName);
        File imageFileNewLoc = null;
        if (overwrite) {
            imageFileNewLoc = new File(MetUtilHelper.convertToAbsolutePathToSomething(imageDir2), imageName2);
        } else {
            // E://bla bla bla + /news/images/news01/20101228163802pOI5.jpg
            imageFileNewLoc = new File(MetUtilHelper.convertToAbsolutePathToSomething(newImageLocation), imageName);
        }


        PairDTO answer = new PairDTO();
        //I Copy and set the new ImageHelper Location
        FileUtil.copyAndDelete(imageFileCurrentLoc, imageFileNewLoc);

        if (overwrite) {
            answer.setOne(imageDir2.concat("/").concat(imageName2)); // news/images/news01/20101228163802pOI5.jpg
        } else {
            answer.setOne(newImageLocation.concat("/").concat(imageName)); // news/images/news01/20101228163802pOI5.jpg
        }

        // E://bla bla bla + /news/images/temp/thumbnails/20101228163802pOI5.jpg
        imageFileCurrentLoc = new File(MetUtilHelper.convertToAbsolutePathToSomething(dirThumbnail));
        // E://bla bla bla + /news/images/news01/thumbnails/20101228163802pOI5.jpg
        imageFileNewLoc = new File(MetUtilHelper.convertToAbsolutePathToSomething(newDirThumbnail));

        //I Copy and set the new ImageHelper thumbnail Location
        FileUtil.copyAndDelete(imageFileCurrentLoc, imageFileNewLoc);

        answer.setTwo(newDirThumbnail); // news/images/news01/thumbnails/20101228163802pOI5.jpg

        return answer;

    }

    public short uploadImage(Integer userId, Integer galleryId, String fileName, String contentType, long fileSize, String galleryDir, InputStream inputStream) throws GalleryException, Exception {
        validateImageInput(fileName, fileSize);

        // Precreate an unique file and then write the InputStream of the uploaded file to it.
        String uploadedFileName = FileUtil.trimFilePath(fileName); // "image.jpg"
        uploadedFileName = MetUtilHelper.getInstance().getUniqueFileName(uploadedFileName); //yyyyMMddssRRRR.jpg

        // absoluteGalleryDir --> E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + galleries\gallery01
        String absoluteGalleryDir = MetUtilHelper.getAbsoluteUserPath(userId).concat(galleryDir);

        // absoluteThumbnailDir --> E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + galleries\gallery01
        String absoluteThumbnailDir = buildAbsoluteThumbnailDir(userId, galleryDir, uploadedFileName);


        File uniqueFile = FileUtil.uniqueFile(new File(absoluteGalleryDir), uploadedFileName);
        File thumbnailFile = new File(absoluteThumbnailDir);

        long startTime = System.currentTimeMillis();
        long endTime;


        FileUtil.write(uniqueFile, inputStream);

        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogHelper.makeLog("One: " + duration);

        startTime = System.currentTimeMillis();

        ImageHelper.processImageDimensions(uniqueFile, MetConfiguration.IMAGE_MAX_WIDTH_SIZE, false); //The image stays in the same place, Im just going to make it smaller.

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        LogHelper.makeLog("Two: " + duration);

        startTime = System.currentTimeMillis();


        ImageHelper.processImageDimensions(uniqueFile, thumbnailFile, MetConfiguration.IMAGE_THUMBNAIL_MAX_WIDTH_SIZE, true);

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        LogHelper.makeLog("Three: " + duration);

        startTime = System.currentTimeMillis();

        short numberOfFiles = (short) MetUtilHelper.countImageFilesInDirectory(absoluteGalleryDir);

        try {
            ImageGallery gallery = this.findImageGalleryById(galleryId);

            //I check if the gallery han an apropiate cover image.
            if (UtilHelper.isStringEmpty(gallery.getCover())
                    || MetConfiguration.DEFAULT_IMAGE_GALLERY_COVER.equalsIgnoreCase(gallery.getCover())) {
                //Id doesn't have an apropiate cover image, or its empty or it has the default cover.
                //So I'll put as cover the image I just uploaded.

                //an exmple of a relativeThumbnailDir --> /users/user00/00/00/01/galleries/gallery20101208235620BaZ8/thumbnails/20101202DFSDF.jpg
                String relativeThumbnailDir = buildRelativeThumbnailDir(userId, galleryDir, uploadedFileName);
                gallery.setCover(MetUtilHelper.removeBackSlashesFromFilePath(relativeThumbnailDir));
            }

            gallery.setNumberPics(numberOfFiles);
            em.merge(gallery);
        } catch (Exception e) {
            //Well I cant do much if this happens, its not that important.
            LogHelper.makeLog(e);
        }

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        LogHelper.makeLog("four: " + duration);


        return numberOfFiles;

    }

    /*
     * It has the same logic to upload an image to News or Event.
     */
    public PairDTO uploadImageWall(String fileName, String contentType, long fileSize, InputStream inputStream) throws GalleryException, Exception {
        // this is where I want to upload this image  -->  temp/images/wall/20110829
        String uploadTempDir = MetConfiguration.WALL_IMAGE_TEMP_DIR.concat("/").concat(MetUtilHelper.getInstance().getCurrentDateString_yyyyMMdd());
        return uploadImage(fileName, fileSize, uploadTempDir, inputStream, ImagesConst.UPLOAD_IMG_WALL);
    }

    public PairDTO uploadImageEvent(String fileName, String contentType, long fileSize, InputStream inputStream) throws GalleryException, Exception {
        return uploadImage(fileName, fileSize, MetConfiguration.EVENTS_IMAGE_TEMP_DIR, inputStream, ImagesConst.UPLOAD_IMG_EVENT);
    }

    public PairDTO uploadImageNews(String fileName, String contentType, long fileSize, InputStream inputStream) throws GalleryException, Exception {
        return uploadImage(fileName, fileSize, MetConfiguration.NEWS_IMAGE_TEMP_DIR, inputStream, ImagesConst.UPLOAD_IMG_NEWS);
    }

    /**
     * I return PairDTO where one = location of the image            -->        ...whatever/directory/imageName.jpg
     *                        two = location of the image thumbnail  -->        ...whatever/directory/thumbnail/imageName.jpg
     * 
     */
    private PairDTO uploadImage(String fileName, long fileSize, String galleryDir, InputStream inputStream, String uploadType) throws GalleryException, Exception {
        validateImageInput(fileName, fileSize);

        // Precreate an unique file and then write the InputStream of the uploaded file to it.
        String uploadedFileName = FileUtil.trimFilePath(fileName); // "image.jpg"
        uploadedFileName = MetUtilHelper.getInstance().getUniqueFileName(uploadedFileName); //yyyyMMddssRRRR.jpg

        //  E://Desarrollo/Dunkelheit/FileSystem/ + temp/newsXXX
        String relativeGalleryDir = galleryDir; //I'll use this later on.
        String absoluteGalleryDir = MetConfiguration.FILE_SYSTEM.concat(MetConfiguration.fileSystemSeparator).concat(galleryDir);
        String absoluteThumbnailDir = absoluteGalleryDir.concat(MetConfiguration.fileSystemSeparator).concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat(MetConfiguration.fileSystemSeparator);

        File uniqueFile = FileUtil.uniqueFile(new File(absoluteGalleryDir), uploadedFileName);
        File uniqueFileThumbnail = new File(absoluteThumbnailDir.concat(uploadedFileName));

        FileUtil.write(uniqueFile, inputStream); //I write the file to disk

        if (ImagesConst.UPLOAD_IMG_EVENT.equalsIgnoreCase(uploadType)) {
            ImageHelper.processImageDimensions(uniqueFile, MetConfiguration.IMAGE_EVENT_MAX_WIDTH_SIZE, false); //I scale to image to the IMAGE_MAX_WIDTH_SIZE which at the ime is 700
            ImageHelper.processImageDimensions(uniqueFile, uniqueFileThumbnail,
                    MetConfiguration.IMAGE_EVENT_THUMBNAIL_MAX_WIDTH_SIZE,
                    MetConfiguration.IMAGE_EVENT_THUMBNAIL_MAX_HEIGHT_SIZE); //Now I see create the thumbnail
        } else if (ImagesConst.UPLOAD_IMG_NEWS.equalsIgnoreCase(uploadType)) {
            ImageHelper.processImageDimensions(uniqueFile, MetConfiguration.IMAGE_NEWS_MAX_WIDTH_SIZE, false); //I scale to image to the IMAGE_MAX_WIDTH_SIZE which at the ime is 700
            ImageHelper.processImageDimensions(uniqueFile, uniqueFileThumbnail,
                    MetConfiguration.IMAGE_NEWS_THUMBNAIL_MAX_WIDTH_SIZE,
                    MetConfiguration.IMAGE_NEWS_THUMBNAIL_MAX_HEIGHT_SIZE); //Now I see create the thumbnail
        } else if (ImagesConst.UPLOAD_IMG_WALL.equalsIgnoreCase(uploadType)) {
            ImageHelper.processImageDimensions(uniqueFile, MetConfiguration.IMAGE_EVENT_MAX_WIDTH_SIZE, false); //I scale to image to the IMAGE_MAX_WIDTH_SIZE which at the ime is 700
            ImageHelper.processImageDimensions(uniqueFile, uniqueFileThumbnail,
                    MetConfiguration.IMAGE_EVENT_THUMBNAIL_MAX_WIDTH_SIZE,
                    MetConfiguration.IMAGE_EVENT_THUMBNAIL_MAX_HEIGHT_SIZE); //Now I see create the thumbnail

        }



        PairDTO answer = new PairDTO();

        //I finaly return  User00/00/00/01/ directory/imageName.jpg
        //here I put the ImageHelper
        answer.setOne(MetUtilHelper.removeBackSlashesFromFilePath(relativeGalleryDir.concat(MetConfiguration.fileSystemSeparator).concat(uniqueFile.getName())));
        //                 User00/00/00/01/ directory/thumbnail/imageName.jpg
        //Here i put the thumbnail
        answer.setTwo(MetUtilHelper.removeBackSlashesFromFilePath(MetUtilHelper.getThumbnailLocation(relativeGalleryDir, uniqueFile.getName())));


        return answer;



    }

    private void validateImageInput(String fileName, long fileSize) throws GalleryException {
        //If it is NOTTTT and ImageHelper then I through an exception.
        if (!MetUtilHelper.isFileAnImage(fileName)) {
            String message = applicationParameters.getResourceBundleMessage("image_upload_info_image_only");
            throw new GalleryException(message, null, message);
        }
        if (fileSize > MetConfiguration.IMAGE_MAX_UPLOAD_SIZE) {
            String message = applicationParameters.getResourceBundleMessage("image_upload_info_image_to_big", fileSize);
            throw new GalleryException(message, null, message);
        }
        if (fileSize == 0) {
            String message = applicationParameters.getResourceBundleMessage("image_upload_info_image_to_small");
            throw new GalleryException(message, null, message);
        }
    }

    public static void main(String[] args) throws Exception {
    }

    /**
     *  an example of a relative Thumbnail Dir --> /users/user00/00/00/01/galleries/gallery20101208235620BaZ8/thumbnails/20101202DFSDF.jpg
     *
     * @param userId      --> 1
     * @param galleryDir  --> /01/galleries/gallery20101208235620BaZ8
     * @param uploadedFileName --> 20101202DFSDF.jpg
     * @return
     */
    private String buildRelativeThumbnailDir(Integer userId, String galleryDir, String uploadedFileName) {
        // to build the dir we are up the following components:
        //  -->/users/user00/00/00
        //  -->/01/galleries/gallery20101208235620BaZ8
        //  -->/thumbnails/
        //  -->20101202DFSDF.jpg
        return MetUtilHelper.getRelativeUserPath(userId).concat(galleryDir).concat("/").concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat("/").concat(uploadedFileName);
    }

    /**
     *  an example of an absolute Thumbnail Dir --> E://Desarrollo/Dunkelheit/FileSystem/users/user00/00/00/01/galleries/gallery20101208235620BaZ8/thumbnails/20101202DFSDF.jpg
     *
     * @param userId      --> 1
     * @param galleryDir  --> /01/galleries/gallery20101208235620BaZ8
     * @param uploadedFileName --> 20101202DFSDF.jpg
     * @return
     */
    private String buildAbsoluteThumbnailDir(Integer userId, String galleryDir, String uploadedFileName) {
        // to build the dir we are up the following components:
        //  -->E://Desarrollo/Dunkelheit/FileSystem/users/user00/00/00
        //  -->/01/galleries/gallery20101208235620BaZ8
        //  -->/thumbnails/
        //  -->20101202DFSDF.jpg
        return MetUtilHelper.getAbsoluteUserPath(userId).concat(galleryDir).concat("/").concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat("/").concat(uploadedFileName);
    }
}
