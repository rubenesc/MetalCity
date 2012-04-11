/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.util;

import co.com.metallium.core.constants.GeneralConstants;
import co.com.metallium.core.constants.MessageConst;
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.constants.state.BanTime;
import co.com.metallium.core.entity.stats.MemoryStat;
import com.metallium.utils.dto.DateHelperDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.regex.ImageValidator;
import com.metallium.utils.utils.FormatHelper;
import com.metallium.utils.utils.ImageExtentionFileFilter;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.HtmlSanitizer;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import co.org.apache.commons.lang.RandomStringUtilsExt;
import com.ocpsoft.pretty.faces.util.StringUtils;

/**
 * 20100901
 * @author Ruben
 */
public class MetUtilHelper extends RegexUtil {

    FormatHelper formatHelper = new FormatHelper();

    private MetUtilHelper() {
    }

    //Singleton Lazy Loader.
    public static MetUtilHelper getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        private static final MetUtilHelper instance = new MetUtilHelper();
    }

    public static boolean isFileAnImage(String fileName) {

        boolean answer = false;
        try {
            answer = ImageValidator.validate(fileName.trim());
        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
        return answer;
    }

    /**
     * It creates a Date from a DTO which has in seperate arguments the Year, Month and Day,
     * so with these arguments it should return a Date Object
     * @param dto with the year, the month and the day.
     * @return Date
     * @throws Exception
     */
    public Date createDateFromString(DateHelperDTO dto) throws Exception {
        String yyyy = dto.getYear();
        String MM = dto.getMonth();
        String dd = dto.getDay();
        Calendar c = new GregorianCalendar(Integer.parseInt(yyyy), Integer.parseInt(MM), Integer.parseInt(dd));
        return c.getTime();
    }

    public static boolean isContentTypeImage(String mimeType) {
        boolean answer = false;

        //String mimeType = JsfUtil.getMimeType(fileName); //fileName = mypic.jpg ---> image/jpg
        if (mimeType != null && mimeType.startsWith("image")) {
            answer = true;
        }

        return answer;
    }

    /**
     *
     *
     * If I upload "image333.jpg" then it returns a file name with the following pattern:
     *
     * yyyyMMddssRRRR.jpg
     *
     * which RRRR means: 4 digit random alpha numeric number.
     *
     * @return
     */
    public String getUniqueFileName(String fileName) {

        String extension = getFileExtention(fileName); // extension = jpg

        if (!UtilHelper.isStringEmpty(extension)) {
            extension = ".".concat(extension);  //        extention = .jpg
        }

        return formatHelper.format_yyyyMMddHHmmss(new Date()).concat(RandomStringUtilsExt.randomAlphanumeric(4)).concat(extension);

    }

    /**
     * Returns the extention of a file (in lowercase) if there is any.
     *
     * Example: image333.jpg  --> jpg
     *          image333.jPg  --> jpg
     *          image333  -->
     *
     * @param fileName:
     * @return
     */
    public static String getFileExtention(String fileName) {
        String extension = null;

        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } else {
            extension = "";
        }

        return extension;

    }

    /**
     * @param   fileName = myPic.gif
     * @param newName = myNewPic
     * @return String = nyNewPic.gif
     */
    public static String getNewFileNameKeepExtention(String fileName, String newName) {
        String extension = null;

        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        } else {
            extension = "";
        }

        return newName.concat(extension);
    }

    /**
     * Returns a String with the format
     *
     * yyyyMMddssRRRR
     *
     * which RRRR means: 4 digit random alpha numeric number.
     *
     * @return
     */
    public String getUniqueNameBasedOnDate() {
        return formatHelper.format_yyyyMMddHHmmss(new Date()) + RandomStringUtilsExt.randomAlphanumeric(4);
    }

    /**
     * Returns a String with the current date format as follows
     * yyyyMMdd
     *
     * -->    20110829  Ago 29 2011
     * -->    20110301  Mar 01 2011
     * -->    20111224  Dic 24 2011
     *
     * @return
     */
    public String getCurrentDateString_yyyyMMdd() {
        return formatHelper.format_yyyyMMdd(new Date());
    }

    /**
     *  This method is invoked when the application is going to expose an image
     * on a web page. SO ... lets say you have the relative an image in a gallery
     * like for example: /users/user00/00/00/01/galleries/gallery20101130124416F0E9/20101125164314K14l.jpg
     *
     * you can only do so, with the Image Servlet path, so it would be something like this.
     *
     * /metallium/image/users/user00/00/00/01/galleries/gallery20101130124416F0E9/20101125164314K14l.jpg
     *
     * you need to add /metallium/image to the relative image path so you can display it.
     *
     * What this method does is that it constructs everything for you,
     * the relative image path, plus it adds the image servlet path,
     * so what comes out of here is ready to be displayed.
     *
     * This Needs to be done because the Images are outside the web application context,
     * they are in some directory different than the ones of the web application.
     * 
     */
    public static String getImageServletRelativePathToImage(Integer userId, String galleryDirectory, String imageName) {
        String answer = null;
        String imagePath = MetUtilHelper.getRelativeUserPath(userId);
        answer = MetUtilHelper.removeBackSlashesFromFilePath(MetConfiguration.WEB_IMAGE_SERVLET_PATH.concat(imagePath).concat(galleryDirectory).concat("/").concat(imageName));
        return answer;
    }

    /**
     * Does the same as the method "getImageServletRelativePathToImage" only that
     * it doesn't attach the Image Servlet Path
     *
     * The album cover is the THUMBNAIL not the Image itself.
     *
     */
    public static String getRelativePathToUserAlbumCover(Integer userId, String galleryDirectory, String imageName) {
        String answer = null;
        String imagePath = MetUtilHelper.getRelativeUserPath(userId);
        answer = MetUtilHelper.removeBackSlashesFromFilePath(imagePath.concat(galleryDirectory).concat("/").concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat("/").concat(imageName));
        return answer;
    }

    /**
     *
     * This is the location of the users Profile Images
     *
     * .../00/00/01/profile
     *
     * @param userId
     * @return
     */
    public static String getRelitivePathToUserProfile(Integer userId) {
        String answer = null;
        answer = getRelativeUserPath(userId).concat("/").concat(UserCommon.FILE_SYSTEM_USER_PROFILE);
        return answer;
    }

    /**
     *
     * Its the absoulte user directory path in the file system
     * example:
     *
     * E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\
     * 
     */
    public static String getAbsoluteUserPath(Integer userId) {
        String userPath = getRelativeUserPath(userId); // User00\00\00\01\
        return convertToAbsolutePathToSomething(userPath); //--> E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\
    }

    /***
     *
     * @param imageDir User00\00\00\01\gallery\galleryXXX
     * @param imageName imageXXX.jpg
     * @return User00\00\00\01\gallery\galleryXXX\thumbnails\imageXXX.jpg
     */
    public static String getThumbnailLocation(String imageDir, String imageName) {
        if (imageDir.endsWith("/")) {
            return imageDir.concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat("/").concat(imageName);
        } else {
            return imageDir.concat("/").concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat("/").concat(imageName);
        }
    }

    /***
     * input folder --> buildActivationUrl/news/images
     * return       --> E://Desarrollo/Dunkelheit/FileSystem/buildActivationUrl/news/images
     *
     * input folder --> buildActivationUrl/news/images/image.jpg
     * return       --> E://Desarrollo/Dunkelheit/FileSystem/buildActivationUrl/news/images/image.jpg
     *
     *
     */
    public static String convertToAbsolutePathToSomething(String folder) {

        StringBuilder sb = new StringBuilder();
        sb.append(MetConfiguration.FILE_SYSTEM); // --> E://Desarrollo/Dunkelheit/FileSystem/
        sb.append(MetConfiguration.fileSystemSeparator);
        sb.append(folder); //--> E://Desarrollo/Dunkelheit/FileSystem/ + folder
        return MetUtilHelper.removeDoubleSlashesFromFilePath(sb.toString());  //--> E://Desarrollo/Dunkelheit/FileSystem/ + folder + /
    }

    /**
     * Based on the Users ID which is an auto numeric sequential number
     * it will give a unique structured path so he can put his fucking shit in it.
     *
     *  example: for an Id of 9123456 --> \\users\\user09\\12\\34\\56\\
     *           for an Id of 1       --> \\users\\user00\\00\\00\\01\\
     *
     *  but i return it without any back slash: /users/user00/00/00/01/
     * @param user Id
     * @return generated Path
     */
    public static String getRelativeUserPath(Integer userId) {
        StringBuilder sb = new StringBuilder();
        String sNumber = userId.toString();
        int size = sNumber.length();
        String fileSeparator = System.getProperty("file.separator");
        String constante1 = UserCommon.FILE_SYSTEM_USER_CONSTANT_1;
        String constante2 = UserCommon.FILE_SYSTEM_USER_CONSTANT_2;
        String millons = "0"; //millons
        String thousands = "00"; //thousands
        String hundreds = "00"; //hundres
        String tens = "00"; //tens
        int round = 0;

        sb.append(constante1);
        sb.append(fileSeparator);
        sb.append(constante2);
        round = 6; // (9123456) --> (09)
        if (size > round) {
            millons = round(size, round, sNumber);
            sb.append(millons);
        } else {
            sb.append("00");
        }

        sb.append(fileSeparator);
        round = 4; // (9123456) --> (12)
        if (size > round) {
            thousands = round(size, round, sNumber);
        }

        sb.append(thousands);
        sb.append(fileSeparator);
        round = 2; // (9123456) --> (34)
        if (size > round) {
            hundreds = round(size, round, sNumber);
        }

        sb.append(hundreds);
        sb.append(fileSeparator);
        round = 0; // (9123456) --> (56)
        if (size > round) {
            tens = round(size, round, sNumber);
        }

        sb.append(tens);
        sb.append(fileSeparator);

        //Finally I return the string without any back slash.
        return MetUtilHelper.removeBackSlashesFromFilePath(sb.toString());


    }

    private static String round(int size, int round, String sNumber) {
        String answer = null;
        String sTemp = null;
        if (size == (round + 1)) {
            sTemp = sNumber.substring(size - (round + 1), size - round);
        } else {
            sTemp = sNumber.substring(size - (round + 2), size - round);
        }
        answer = DataHelper.colocarCaracteresALaIzquierda('0', 2, sTemp);
        return answer;
    }

    /**
     * Creates a new and empty directory in the default temp directory using the
     * given prefix. This methods uses {@link File#createTempFile} to create a
     * new tmp file, deletes it and creates a directory for it instead.
     *
     * @param prefix The prefix string to be used in generating the diretory's
     * name; must be at least three characters long.
     * @return A newly-created empty directory.
     * @throws IOException If no directory could be created.
     */
    public static File createDir(String fileSystemPath) throws IOException {

        if (fileSystemPath == null) {
            throw new IOException("No directory is specified");
        }

        File dir = new File(fileSystemPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Unable to create the user directory " + dir);
            }
        } else {
            throw new IOException("Unable to create the user directory because it is already created: " + dir);
        }

        return dir;
    }

    public static void deleteFile(String filePath) throws IOException {

        if (filePath == null) {
            throw new IOException("No file is specified");
        }

        File file = new File(filePath);

        if (file.exists()) {

            if (!file.isFile()) {
                throw new IOException("Unable to delete: " + file + ", because its not a valide file.");
            }

            boolean deleted = file.delete();

            if (!deleted) {
                throw new IOException("Unable to delete the file: " + file);
            }

        } else {
            //Well if it doesnt exist then I dont care.
            //throw new IOException("Unable to delete the file because it is doesn't exist: " + file);
        }

    }

    public void deleteDir(String fileSystemPath) throws IOException {

        if (fileSystemPath == null) {
            throw new IOException("No directory is specified");
        }

        File dir = new File(fileSystemPath);

        if (dir.exists()) {

            boolean deleted = this.deleteDirectory(dir);

            if (!deleted) {
                throw new IOException("Unable to delete the user directory " + dir);
            }

        } else {
            throw new IOException("Unable to delete the user directory because it is doesn't exist: " + dir);
        }

    }

    public void deleteFilesInDir(String fileSystemPath) throws IOException {

        if (fileSystemPath == null) {
            throw new IOException("No directory is specified");
        }

        File dir = new File(fileSystemPath);

        if (dir.exists()) {

            this.deleteFilesInDirectory(dir);

        } else {
            throw new IOException("Unable to delete the user directory because it is doesn't exist: " + dir);
        }

    }

    /**
     * If you enter c://abc/stuff/mydir
     * it deletes "myDir"
     * leaving like this --> c://abc/stuff/
     *
     * @param path
     * @return
     */
    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    /**
     * If you enter c://abc/stuff/mydir
     * it deletes all the files from "myDir" on and all the sub directories it has
     * leaving like this --> c://abc/stuff/mydir
     * it doesnt delete "mydir"
     *
     */
    private void deleteFilesInDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * Given a file system directory path, it should be able to count the number
     * of Image files in given directory.
     * 
     */
    public static int countImageFilesInDirectory(String directory) throws Exception {
        int answer;
        File filesFolder = new File(directory);
        if (filesFolder.isDirectory()) {
            answer = filesFolder.listFiles(ImageExtentionFileFilter.getInstance()).length;
        } else {
            //This isn't supposed to happen
            throw new Exception("This isn't supposed to happen, this is should be a directory: " + directory);
        }
        return answer;
    }

    /**
     * Given a file system directory path, it should be able to count the number
     * of directories in given directory.
     *
     */
    public static int countFoldersInDir(String directory) throws Exception {
        int answer = 0;
        File f = new File(directory);
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                if (file.isDirectory()) {
                    answer++;
                }
            }
        }
        return answer;

    }

    /**
     * Helper class that returns a Date which is certain minutes behind the actual time
     * in order to get all the comments a person has made in the last say for example 5 mintues
     *
     * So for example right now its 9:16:00 pm
     * it will return 9:11:00 pm
     *
     * depending on the configuration in the minutes validation.
     *
     * @return Date
     */
    public static Date getCommentTimeValidationDate() {
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.MINUTE, -MetConfiguration.COMMENTS_TIME_VALIDATION_IN_MINUTES);
        return date.getTime();
    }

    public static Date getNotificationsSinceThisDay() {
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.DAY_OF_YEAR, -MetConfiguration.NOTIFICATION_SEARCH_SINCE_LAST_X_DAYS);
        return date.getTime();
    }

    public static Date determineBanExpirationDate(String banTime) {

        GregorianCalendar date = new GregorianCalendar();

        if (BanTime.isBan5Minutes(banTime)) {
            date.add(Calendar.MINUTE, 5);
        } else if (BanTime.isBan30Minutes(banTime)) {
            date.add(Calendar.MINUTE, 30);
        } else if (BanTime.isBan1Hour(banTime)) {
            date.add(Calendar.HOUR_OF_DAY, 1);
        } else if (BanTime.isBan1Day(banTime)) {
            date.add(Calendar.DAY_OF_YEAR, 1);
        } else if (BanTime.isBanIndefinitely(banTime)) {
            return null;
        }

        return date.getTime();
    }

    public static Date getEventsSearchDateLimit() {
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.DAY_OF_YEAR, MetConfiguration.EVENT_SEARCH_NEXT_X_DAYS);
        return date.getTime();
    }

    /**
     * Helper class that tells me if the state of the message is read or unread
     * @param readState = True if message is already read. False if message is unread.
     *
     */
    public static boolean isMessageRead(Integer readState) {
        if (UtilHelper.areIntegersEqual(MessageConst.read, readState)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Builds the activation url path that is sent to the user in an email.
     * Example: http://www.metalcity.net/metallium/user/activate?email=user@gmail.com
     *
     * @param email
     * @return
     */
    public static String buildActivationUrl(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append(getWebDomainName()); //--> This should be --> http://www.metalcity.net
        sb.append(MetConfiguration.WEB_USER_ACTIVATION_SERVLET_PATH); //--> /user/activate
        sb.append("?");
        sb.append(GeneralConstants.ACTIVATION_EMAIL_PARAM);
        sb.append("=");
        sb.append(email);
        return sb.toString();
    }

    /**
     * Builds the url path that belongs to the users profile page.
     * Example: http://www.metalcity.net/metallium/sear.bliss
     *
     * @param nick --> users nick
     * @return url that belongs to the users profile.
     */
    public static String buildUserProfilePageUrl(String nick) {
        StringBuilder sb = new StringBuilder();
        sb.append(getWebDomainName()); //--> This should be --> http://www.metalcity.net
        sb.append("/"); // --> /
        sb.append(nick); //-->  sear.bliss
        return sb.toString();
    }

    /**
     * Builds the url path that belongs to a particular forum.
     * Example: http://www.metalcity.net/metallium/forum/best-metal-bands
     *
     * @param alias --> 'best-metal-bands' the alias of the forum
     * @return url to reach the forum
     */
    public static String buildForumPageUrl(String alias) {
        StringBuilder sb = new StringBuilder();
        sb.append(getWebDomainName()); //--> This should be --> http://www.metalcity.net
        sb.append("/"); // --> /
        sb.append("forum"); //-->
        sb.append("/"); // --> /
        sb.append(alias); //-->  sear.bliss
        return sb.toString();
    }

    /**
     *
     * @param hey-dude: like 123 ''--  ... []{}this is a string
     * @return hey-dude-like-123-this-is-a-string
     *
     */
    public static String generateAlias(String title) {

        if (StringUtils.isNotBlank(title)) {
            //Replace Special Characters from the String.
            title = title.toLowerCase().replace('à', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace('ñ', 'n');

            title = removeNonAlphanumericCharacters(title);
            if (title.length() > 61) {
                title = title.substring(0, 60);
            }
        }
        return title;

    }

    //   news/images/news
    /**
     * Helper method that determines in what directory should I upload an image
     * for an event or news.
     *
     * @param dir = relative URL of the Event or News directory. This folder is still incomplete.
     * @param type = 'event' or 'news'
     * @return dir = relative URL of the Event or News directory to upload the image
     * @throws Exception
     */
    public static String getUploadImageDir(String dir, String type, Integer userId) throws Exception {

        //Two examples

        //dir -->  news/images/001/001
        //type --> 'news'

        //dir --> wall
        //type --> 'images'


        int numOfFolders = 0;
        if (userId == null) {
            //Example: I count in E://Desarrollo/Dunkelheit/FileSystem/news/images/001/001
            numOfFolders = countFoldersInDir(MetConfiguration.FILE_SYSTEM.concat(dir)); //   12
        } else {
            //If a User Id comes then I know I its because I have to search in the Users Path that refers to the user id.
            dir = MetUtilHelper.getRelativeUserPath(userId).concat(dir);
            //Example: I count in E://Desarrollo/Dunkelheit/FileSystem/users/user00/00/00/01/wall
            numOfFolders = countFoldersInDir(MetUtilHelper.getAbsoluteUserPath(userId).concat(dir)); //   12
        }

        if (numOfFolders == 0) {
            // the folder 000 should not exist. It should always start from 1.
            numOfFolders = 1;
        }


        //
        //now I count the images in  --> news/images/001/001 + /news + 012 --> news/images/001/001/news012
        String dir2 = dir.concat("/").concat(type).concat(DataHelper.colocarCaracteresALaIzquierda('0', 3, "" + numOfFolders));

        int numOfFiles = 0;
        try {
            numOfFiles = countImageFilesInDirectory(MetConfiguration.FILE_SYSTEM.concat(dir2)); // ...filesystem/news/images/news012
        } catch (Exception e) {
            //I dont care
        }

        if (numOfFiles > 300) {
            //the dir2 --> news/images/001/001/news012  has too many files I have to create a new directory
            //which will be --> news/images/001/001/news013
            numOfFolders = numOfFolders + 1;
            dir2 = dir.concat("/").concat(type).concat(DataHelper.colocarCaracteresALaIzquierda('0', 3, "" + numOfFolders));
        }

        return dir2;

    }

    /**
     *
     * example: This should be --> http://www.metalcity.net
     *          This should be --> http://www.metalcity.net/metallium
     *
     * @return
     */
    public static String getWebDomainName() {

        if (MetConfiguration.WEB_CONTEXT_ENABLED) {
            return MetConfiguration.WEB_DOMAIN_NAME.concat(MetConfiguration.WEB_CONTEXT_PATH);
        } else {
            return MetConfiguration.WEB_DOMAIN_NAME;
        }
    }

    public static MemoryStat getMemoryStats() {

        //Declare the size
        int megaBytes = 1024 * 1024;
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        MemoryStat answer = new MemoryStat();

        short usedMemory = (short) ((runtime.totalMemory() - runtime.freeMemory()) / megaBytes);
        short maxMemory = (short) (runtime.maxMemory() / megaBytes);
        short freeMemory = (short) (runtime.freeMemory() / megaBytes);
        short totalMemory = (short) (runtime.totalMemory() / megaBytes);
        short percent = (short) (usedMemory * 100 / maxMemory);

        answer.setPercentMemoryUsed(percent);
        answer.setMemoryUsed(usedMemory);
        answer.setMemoryFree(freeMemory);
        answer.setMemoryTotal(totalMemory);
        answer.setMemoryMax(maxMemory);
        answer.setDate(new Date());
        answer.setActiveWebSessions(StaticApplicationParameters.getActiveWebSessions());

        return answer;
    }

    public static void main(String args[]) {
        //isFileImageTest();
        //deleteDirTest();
        //fileExtentionTest();
    }

    private static void fileExtentionTest() {

        String file = "fuckyou.";
        String extention = getFileExtention(file);
        LogHelper.makeLog("File: " + file + ", ext: " + extention);

    }

    private static void deleteDirTest() {
        LogHelper.makeLog("... Start ...");
        String dir = "E://Desarrollo//Dunkelheit//FileSystem//users//user00//00//00//01//profile";
        try {
            MetUtilHelper.getInstance().deleteDir(dir);
        } catch (IOException ex) {
            LogHelper.makeLog(ex);
        }

    }

    private static void isFileImageTest() {

        String file = MetConfiguration.FILE_SYSTEM + "/abc.jpg";

        System.out.println("File --> " + file);
        if (isFileAnImage(file)) {
            System.out.println("It an image");
        } else {
            System.out.println("Is NOT an image");
        }
    }
}
