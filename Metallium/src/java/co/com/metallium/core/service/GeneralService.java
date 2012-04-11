/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.UserCommon;
import co.com.metallium.core.entity.Configuration;
import co.com.metallium.core.entity.stats.MemoryStat;
import co.com.metallium.core.exception.EmailException;
import co.com.metallium.core.exception.InitializationException;
import co.com.metallium.core.service.dto.EmailDTO;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import co.org.apache.commons.lang.RandomStringUtilsExt;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 20101102
 * @author Ruben
 */
@Singleton
public class GeneralService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;
    @PersistenceContext(unitName = "MetalliumStatsPU")
    private EntityManager emStats;
    @Resource(name = "mail/metallium")
    private javax.mail.Session mailSession;
    @EJB
    CommonService commonService;
    @EJB
    ApplicationParameters applicationParameters;

    public void initializeApplicationParameters() throws InitializationException {
        try {

            this.initializeMetalliumConfiguration(commonService.findConfigurationList());
            ApplicationParameters.initializeSexList();
            ApplicationParameters.initializeBirthdayDisplayList();
            ApplicationParameters.initializeBanTimeList();
            ApplicationParameters.initializeVisibilityList();
            ApplicationParameters.initializeLocaleList();
            applicationParameters.initializeNewsStateList();
            applicationParameters.initializeUserStateList();
            ApplicationParameters.initializedDateList();
            ApplicationParameters.initializeCountryList(commonService.findCountryList());
            ApplicationParameters.initializeProfileList(commonService.findProfileList());
            ApplicationParameters.initializeNetworkList(commonService.findNetworkList());
            ApplicationParameters.initializeForumCategoryList(commonService.findForumCategoryList());
            ApplicationParameters.initializeReservedNamesList(commonService.findReservedNamesList());

        } catch (Exception e) {
            throw new InitializationException(e.getMessage(), e);
        }
    }

    public void createUserFileSystem(int userId) throws IOException {

        String userPath = MetUtilHelper.getAbsoluteUserPath(userId); // E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\
        MetUtilHelper.createDir(userPath.concat(UserCommon.FILE_SYSTEM_USER_PROFILE)); // -- E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\profile
        MetUtilHelper.createDir(userPath.concat(UserCommon.FILE_SYSTEM_USER_GALLERIES)); // -- E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\galleries
        MetUtilHelper.createDir(userPath.concat(UserCommon.FILE_SYSTEM_USER_STUFF)); // -- E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\stuff

    }

    public void deleteGalleryFromFileSystem(int userId, String directory) throws IOException {
        MetUtilHelper helper = MetUtilHelper.getInstance();
        String galleryPath = MetUtilHelper.getAbsoluteUserPath(userId).concat(directory); // E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\galleries\gallery20101130124416F0E9
        helper.deleteDir(galleryPath);
    }

    public void deleteImageFromFileSystem(int userId, String directory, String fileName) throws IOException {

        // E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + galleries\gallery20101130124416F0E9 + \
        String galleryPath = MetUtilHelper.getAbsoluteUserPath(userId).concat(directory).concat(MetConfiguration.fileSystemSeparator);

        // E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\galleries\gallery20101130124416F0E9\  + image01.jpg
        String imagePath = galleryPath.concat(fileName);

        // E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\ + galleries\gallery20101130124416F0E9\ + thumbnails  + \image01.jpg
        String imageThumbnailPath = galleryPath.concat(MetConfiguration.fileSystemSeparator).concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS).concat(MetConfiguration.fileSystemSeparator).concat(fileName);

        MetUtilHelper.deleteFile(imagePath);
        MetUtilHelper.deleteFile(imageThumbnailPath);
    }

    public void deleteAllImagesInFolderFromFileSystem(String dirPath) throws IOException {
        MetUtilHelper helper = MetUtilHelper.getInstance();
        helper.deleteFilesInDir(dirPath);
    }

    /**
     * Creates a gallery directory for the user given.
     * It takes the User Id and based on his ID it locates the user assigned
     * file path, so we can create a new gallery folder based on our naming conventions.
     *
     * it will create the new directory for example in
     * E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\galleries\gallery20101130ABCD
     *
     * and will return the name of the folder
     * return String gallery20101130ABCD
     *
     */
    public String createUserFileSystemGallery(int userId) throws IOException {

        //Ok here we create the name of the Folder of the Gallary we are creating
        //So it goes out like this "galleries/gallery20101202ABCD"
        //I use "concat" because its more eficient than using "+".
        //Also this is the name Im going to store in the DB for the Image Gallery "directory"
        String createdGalleryFolderName = UserCommon.FILE_SYSTEM_USER_GALLERIES.concat("/").concat(UserCommon.FILE_SYSTEM_USER_GALLERY).concat(MetUtilHelper.getInstance().getUniqueNameBasedOnDate());

        StringBuilder sbFileSystemPathDir = new StringBuilder();
        String userPath = MetUtilHelper.getRelativeUserPath(userId); //--> User00\00\00\01\

        sbFileSystemPathDir.append(MetConfiguration.FILE_SYSTEM); // --> E://Desarrollo/Dunkelheit/FileSystem/
        sbFileSystemPathDir.append(MetConfiguration.fileSystemSeparator);
        sbFileSystemPathDir.append(userPath); //--> E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\
        sbFileSystemPathDir.append(MetConfiguration.fileSystemSeparator);
        sbFileSystemPathDir.append(createdGalleryFolderName); //--> E://Desarrollo/Dunkelheit/FileSystem/User00\00\00\01\galleries\gallery20101130ABCD

        MetUtilHelper.createDir(sbFileSystemPathDir.toString());
        MetUtilHelper.createDir(sbFileSystemPathDir.toString().concat(MetConfiguration.fileSystemSeparator).concat(UserCommon.FILE_SYSTEM_USER_THUMBNAILS));

        return createdGalleryFolderName;
    }

    //===========================MAIL==========================================//
    public void sendEmail(String email, Object[] arguments, String bodyTemplate, String subjectTemplate) throws EmailException {
        try {

            String body = applicationParameters.getResourceBundleMessage(bodyTemplate, arguments);
            String subject = applicationParameters.getResourceBundleMessage(subjectTemplate);
            EmailDTO enviarCorreoDTO = new EmailDTO();

            if (!MetConfiguration.ARE_WE_IN_DEVELOPMENT) {
                enviarCorreoDTO.setEmailAddress(new String[]{email});
            } else {
                enviarCorreoDTO.setEmailAddress(new String[]{"ruben.escudero@gmail.com"}); //XXX mean while.
            }

            enviarCorreoDTO.setBody(body);
            enviarCorreoDTO.setSubject(subject);
            sendEmail(enviarCorreoDTO);
        } catch (Exception e) {
            throw new EmailException(e.getMessage(), e);
        }
    }

    @Asynchronous
    private void sendEmail(EmailDTO dto) throws Exception {
        try {
            String mailPassword = mailSession.getProperty("mail.smtp.password");
            String mailUser = mailSession.getProperty("mail.smtp.user");

            MimeMessage message = crearMensaje(dto);

            //If there is no password at all, then it means I dont have to Connect to the mail service and
            //loggin, I just have to simply send the email and thats it.
            if (UtilHelper.isStringEmpty(mailPassword)) {
                Transport.send(message, message.getAllRecipients());
            } else {
                Transport transport = null;
                try {
                    transport = mailSession.getTransport("smtp");
                    transport.connect(mailUser, mailPassword);
                    transport.sendMessage(message, message.getAllRecipients());
                } finally {
                    try {
                        if (transport != null) {
                            transport.close();
                        }
                    } catch (Exception exxx) {
                        LogHelper.makeLog(exxx);
                    }
                }
            }
        } catch (Exception ex) {
            LogHelper.makeLog("An error occurred trying to send an email : " + ex.getMessage(), ex);
        }
    }

    private MimeMessage crearMensaje(EmailDTO emailDTO) throws MessagingException {

        // Creo el mensaje
        MimeMessage message = new MimeMessage(mailSession);
        // Se construye el mensaje
        StringBuilder emailAddresses = new StringBuilder(100);

        for (int i = 0; i < emailDTO.getEmailAddress().length; i++) {
            if (i != 0) {
                emailAddresses.append(",");
            }
            emailAddresses.append(emailDTO.getEmailAddress()[i]);
        }
        String toAdress = emailAddresses.toString();

//        message.setFrom(new InternetAddress("bot@metalcity.net"));
        message.setRecipients(RecipientType.TO, InternetAddress.parse(toAdress, false));
//        message.setRecipients(RecipientType.CC, InternetAddress.parse(emailDTO.getCcAddress(), false));
//        message.setRecipients(RecipientType.BCC, InternetAddress.parse(emailDTO.getBccAddress(), false));

        if (UtilHelper.isStringEmpty(emailDTO.getSubject())) {
            emailDTO.setSubject(applicationParameters.getResourceBundleMessage("general_pagename"));
        }


        message.setSubject(emailDTO.getSubject());
        //      message.setDisposition(emailDTO.getDisposition());
//        message.setDescription(emailDTO.getDescription());
        message.setSentDate(new Date());

        MimeBodyPart textPart = new MimeBodyPart();

        if (UtilHelper.isStringEmpty(emailDTO.getBody())) {
            emailDTO.setBody("xxx");
        }
        textPart.setContent(emailDTO.getBody(), "text/html; charset=ISO-8859-1");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(textPart);

        //Process Attachments
//        ArrayList<MimeBodyPart> mimeAttachments = procesarAttachments(emailDTO.getAttachments());
//        for (Iterator<MimeBodyPart> it = mimeAttachments.iterator(); it.hasNext();) {
//            MimeBodyPart mimeBodyPart = it.next();
//            mp.addBodyPart(mimeBodyPart);
//        }

        message.setContent(mp);

        return message;
    }

    /**
     * Here I initialize the class MetConfiguration which contains the configuration of
     * the application. Like where the file system is, sizes of images, search parameters, etc.
     *
     * Without this class the application could simply not work at all. So I initialize its values
     * from the DB. What I do is that the class already has some default values, so I override them
     * with the values that are in the database. If there are no values in the DB then I stay with
     * the default values. If there are values in the DB then I update only the values that are in the
     * DB.
     *
     * I do it with reflection. So if in the DB there is the value 'FILE_SYSTEM'
     * then I set it in the class --> MetConfiguration.FILE_SYSTEM = newValue
     * so in the application If I need to know the file system location I just
     * use. MetConfiguration.FILE_SYSTEM, and thats it. Plain and simple.
     *
     * @param configurationList = Contains the configuration list from the database.
     *
     */
    private void initializeMetalliumConfiguration(List<Configuration> configurationList) throws InitializationException {
        try {

            MetConfiguration objInstante = new MetConfiguration();
            Class cl = MetConfiguration.class;

            for (Iterator<Configuration> it = configurationList.iterator(); it.hasNext();) {
                Configuration configuration = it.next();

                Field field = cl.getDeclaredField(configuration.getId());
                Class type = field.getType();
                if (!UtilHelper.isStringEmpty(configuration.getValue())) {

                    if (type == String.class) {
                        field.set(objInstante, configuration.getValue());
                    } else if (type == int.class || type == Integer.class) {
                        field.set(objInstante, Integer.parseInt(configuration.getValue()));
                    } else if (type == boolean.class || type == Boolean.class) {
                        field.set(objInstante, Boolean.parseBoolean(configuration.getValue()));
                    } else {
                        LogHelper.makeLog("WTF is this --> " + field.getName() + ", type: " + field.getType().getSimpleName() + ". We are in initializeMetalliumConfiguration");
                    }
                }
            }

        } catch (Exception ex) {
            throw new InitializationException(ex);
        }
    }

    /**
     *
     * @param title = hey-dude: like 123 ''--  ... []{}this is a string
     * @param namedQueryCountAlias = "News.countNewsByAlias"
     * @param isCreate = it indicates if we going to look for an alias for a new news or for an existing news
     * @param entityId = if it is for a new news then this has to be null, if its for an existing news then
     *                   this will contain the id of that news we are editing
     * @return hey-dude-like-123-this-is-a-string
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //THis is verry Important when we are going to perform just READ queries 
    public String generateAliasForTitle(String title, String namedQueryCountAlias, boolean isCreate, Integer entityId) {

        title = MetUtilHelper.generateAlias(title); //Here I should have a string no longer than 60 characters

        boolean isAliasTaken = this.isAliasTaken(namedQueryCountAlias, title, isCreate, entityId);
        if (isAliasTaken) {
            //if it is taken then I just add an extra random number
            title = title.concat("-").concat(RandomStringUtilsExt.randomNumeric(4));
        }

        return title;
    }

    /**
     * @param namedQueryCountAlias = "News.countNewsByAlias"
     * @param alias = hey-dude-like-123-this-is-a-string
     * @param isCreate = it indicates if we going to look for an alias for a new news or for an existing news
     * @param entityId = if it is for a new news then this has to be null, if its for an existing news then
     *                   this will contain the id of that news we are editing
     * @return true || false
     */
    private boolean isAliasTaken(String namedQueryCountAlias, String alias, boolean isCreate, Integer entityId) {
        boolean answer = true;
        try {


            Query q = null;

            if (isCreate) {
                q = em.createNamedQuery(namedQueryCountAlias.concat("Create"));
                q.setParameter("alias", alias);
            } else {
                q = em.createNamedQuery(namedQueryCountAlias.concat("Edit"));
                q.setParameter("alias", alias);
                q.setParameter("id", entityId);
            }


            int count = ((Number) q.getSingleResult()).intValue();

            if (count == 0) {
                answer = false; //the alias is not taken
            }

        } catch (Exception e) {
            //I cant to anything.
            LogHelper.makeLog(e.getMessage(), e);
        }

        return answer;
    }

    @Asynchronous
    public void logMemoryStat() {
        try {
            MemoryStat memoryStat = MetUtilHelper.getMemoryStats();
            emStats.persist(memoryStat);
        } catch (Exception ex) {
            LogHelper.makeLog("Error: " + ex.getMessage(), ex);
        }
    }

    public List<MemoryStat> getMemoryStatForReport() {
        List<MemoryStat> list = null;

        try {
            Query q =  emStats.createNamedQuery("MemoryStat.findStatsForReport");
            q.setMaxResults(20);
            list = q.getResultList();

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
        return list;


    }
}
