/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main;

import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.regex.ImageValidator;
import com.metallium.utils.utils.FormatHelper;
import com.metallium.utils.utils.UtilHelper;
import com.metallium.utils.utils.htmlscriptvalidator.ExternalResourceBlocker;
import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import co.org.apache.commons.lang.RandomStringUtilsExt;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 *
 * @author Ruben
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public final static Integer ADMINISTRATOR1 = 100;
    public static final String FILE_SYSTEM = "E://Desarrollo/Dunkelheit/FileSystem/";
    // "/metallium/index.jsf";   Simple but works for me
    public static final Pattern indexPagePattern = Pattern.compile(".*?[/]index[.].*?", Pattern.CASE_INSENSITIVE);
    public static final Pattern imageNamePattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif))$)", Pattern.CASE_INSENSITIVE);
    public static final Pattern namePattern = Pattern.compile("(^[A-Za-z](?=[A-Za-z0-9 _.]{3,31}$)[a-zA-Z0-9 _]*.?[a-zA-Z0-9_]*$)", Pattern.CASE_INSENSITIVE);
    public static final Pattern nickPattern = Pattern.compile("(^[a-z0-9](?=[a-z0-9.]{1,60}$)[a-z0-9]*.?[a-z0-9]*$)", Pattern.CASE_INSENSITIVE);
    public static final Pattern numberPattern = Pattern.compile("[0-9]*");
    public static final Pattern urlPartsPattern = Pattern.compile(".*?([^.]+\\.[^.]+)");
    // private static final Pattern youTubePattern = Pattern.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*", Pattern.CASE_INSENSITIVE);
    private static final Pattern youTubePattern1 = Pattern.compile("^[^v]+v=(.{11}).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern youTubePattern2 = Pattern.compile(".*youtu.be/(.{11}).*", Pattern.CASE_INSENSITIVE);
    public static Boolean autenticated;

    public static void main(String[] args) {


        if (false) {
            junk();
        }
        
        //doesFileHaveValidImageExtention();
        //testTransformComment();
        //        testRemoveBackSlashesFromFilePath();
        //        generateUserId();
        //        testBuildDate_yyyyMMddWithPadding();
        //        testGetYouTubeVideoId();
        //        getDomainFromUrl();
        //        validateYouTubeEmbededVideo();
        //        getClientIpAddressXForwardedFor();
        //        getMemoryStats();
        //        validateNumber();
        //        testSearchOrderBy();
        //        TestStringFormat();
        //        DeleteHtmlInvalidTags();
        //        getFirstHtmlParagraph();
        //        compareTest1();
        //        convertStringToDate();
        //        decomposeDate();
        //        createUserFileSystem();
        //        generateUniqueFileName();
        //        countFoldersInDir();
        //        testIfStringIsGalleryDir();
        //        someStupidSlashRemoveTest();
        //        LogHelper.makeLog("NewPassword: " + generarPasswordAleatorio());
        //        testAddTimeToDate()
        //        isTimeUpTest();
        //        isRequestUriTheIndexPage();
        //        isImageNameValid();
        //        stringEncoding();
        //        urlRexEx();
//                isNickValid();
        //        removeNonAlphanumericCharacters();

//        final long startTime = System.currentTimeMillis();
//        final long endTime;
        //   LogHelper.makeLog("thumbnail: " + generarPasswordAleatorio());
//        endTime = System.currentTimeMillis();
//        final long duration = endTime - startTime;
//        LogHelper.makeLog("TIme: " + duration);
    }

    private static void urlRexEx() {

        String url = "http://localhost.com/vshow/qa/lobby";   //<-- We Need the "qa" part
        url = "http://localhost.com/vshoW/qa?2dsdsdsd/sdfsdffssfs";

        Pattern p = Pattern.compile(".*/vshow/", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);

        if (matcher.find()) {
            //Yes it has the VSHOW pattern
            String showCode = url.toLowerCase().replaceAll(".*/vshow/.*?", "").replaceAll("[//^\\W].*", "");
            System.out.println(showCode);
        } else {
            System.out.println("Nothing Found!!! --> " + url);
        }


    }

    private static void stringEncoding() {
        try {
            String x = "کشاورزی";
            x = "위키백과, 우리 모두의 백과사전.";
            byte[] biteUTF8 = x.getBytes("UTF-8");
            byte[] biteIso = x.getBytes("ISO-8859-1");

            byte[] biteConvert = x.getBytes();

            String xIso = new String(biteIso);
            String xUtf8 = new String(biteIso, "UTF-8");

            LogHelper.makeLog("String normal: " + x);
            LogHelper.makeLog("String bytes: " + new String(biteUTF8));
            LogHelper.makeLog("String UTF8: " + new String(biteUTF8));
            LogHelper.makeLog("String ISO-8859-1: " + xIso);
            LogHelper.makeLog("String convert: " + xUtf8);
        } catch (UnsupportedEncodingException ex) {
            LogHelper.makeLog(ex);
        }


    }

    private static void isImageNameValid() {
        String imageName = "tiffany .jpg";

        imageName = "tiffany.jpg";
        imageName = "s 6666";
        imageName = "I the moder fuckkerrrrrrrrrrrrrr$";
        imageName = "dsfsdfd     ..sf";

        if (namePattern.matcher(imageName).matches()) {
            LogHelper.makeLog("Yessss " + imageName);
        } else {
            LogHelper.makeLog("noooooo " + imageName);
        }

    }

    private static void isNickValid() {

        String nick = "tiffany .jpg";
        nick = "ruben";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben.escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "....";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "23232.";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben|escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben..escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben.escudero.";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = ".ruben.escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben_escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "ruben?escudero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "rubenescud&ero";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));
        nick = "rRuben";
        System.out.println("--nick--> " + nick + " --> " + isNickValid(nick));

    }

    private static String isNickValid(String nick) {

        if (nickPattern.matcher(nick).matches()) {
            return "Yessss";
        } else {
            return "Noooooo";
        }

    }

    private static void isRequestUriTheIndexPage() {

        String uri = "metallium/index.jsf";

        if (indexPagePattern.matcher(uri).matches()) {
            LogHelper.makeLog("Yessss " + uri);
        } else {
            LogHelper.makeLog("noooooo " + uri);
        }

    }

    private static void isTimeUpTest() {

        Date timePast = getTimeInPast();
        Date timeFuture = getTimeInFuture();

        if (UtilHelper.isTimeUp(timePast)) {
            LogHelper.makeLog("Past Time Correct, Time is UP!!!: " + timePast.toString());

        } else {
            LogHelper.makeLog("Past Time Incorrect, Time is UP!!!: " + timePast.toString());
        }

        if (UtilHelper.isTimeUp(timeFuture)) {
            LogHelper.makeLog("Future Time Incorrect, Time is not UP!!!: " + timeFuture.toString());

        } else {
            LogHelper.makeLog("Future Time Correct, Time is not UP!!!: " + timeFuture.toString());
        }

        if (UtilHelper.isTimeUp(null)) {
            LogHelper.makeLog("NULL Time Incorrect, Time is not UP!!!: " + timeFuture.toString());

        } else {
            LogHelper.makeLog("NULL Time Correct, Time is not UP!!!: " + timeFuture.toString());
        }

    }

    private static Date getTimeInPast() {
        GregorianCalendar datePast = new GregorianCalendar();
        datePast.add(Calendar.MINUTE, -5);
        return datePast.getTime();

    }

    private static Date getTimeInFuture() {
        GregorianCalendar dateFuture = new GregorianCalendar();
        dateFuture.add(Calendar.DATE, 2);
        return dateFuture.getTime();

    }

    private static void testAddTimeToDate() {

        GregorianCalendar dateMinus5Minutes = new GregorianCalendar();
        GregorianCalendar datePlus2Days = new GregorianCalendar();
        dateMinus5Minutes.add(Calendar.MINUTE, -5);
        datePlus2Days.add(Calendar.DATE, 2);
        LogHelper.makeLog("Time -5 minutes: " + dateMinus5Minutes.getTime());
        LogHelper.makeLog("Time +2 days: " + datePlus2Days.getTime());

    }

    public static boolean isAdmin1(Integer id) {
        boolean answer = false;
        if (id.compareTo(ADMINISTRATOR1) >= 0) {
            answer = true;
        }
        return answer;
    }

    public static String trimFilePath(String fileName) {
        return fileName.substring(fileName.lastIndexOf("/") + 1).substring(fileName.lastIndexOf("\\") + 1);
    }

    public static PairDTO trimFilePathExt(String fileLocation) {
        PairDTO answer = new PairDTO();
        String fileName = fileLocation.substring(fileLocation.lastIndexOf("/") + 1).substring(fileLocation.lastIndexOf("\\") + 1);
        String fileFolder = fileLocation.substring(0, fileLocation.lastIndexOf("/") + 1).substring(fileLocation.lastIndexOf("\\") + 1);

        answer.setOne(fileFolder);
        answer.setTwo(fileName);


        return answer;
    }

    private static void someStupidSlashRemoveTest() {
        String test = "/aaa//bbb/ccc/ddd///file.jpg";
        LogHelper.makeLog("Convert this --> " + test + ", into this -->" + trimFilePath(test));
    }
    private static Pattern galleryDirectoryPattern = Pattern.compile("[\\\\/]*", Pattern.CASE_INSENSITIVE);

    public static boolean obtailFileName(String mensaje) {
        boolean anwser = false;

        Matcher matcher = galleryDirectoryPattern.matcher(mensaje);
        int x = matcher.groupCount();
        System.out.println("xxx: " + matcher.group(x));


        return anwser;
    }

    private static void testIfStringIsGalleryDir() {
        String a = "galleries///gallery20101130124416F0E9";
        boolean x = isDirectoyAGallery(a);
        if (x) {
            LogHelper.makeLog("Se puede borrar");
        } else {
            LogHelper.makeLog("No Se puede borrar");
        }
    }

    private static void countFoldersInDir() {
        String folder = "E:/";
        File f = new File(folder);
        if (f.isDirectory()) {
            int count = 0;
            for (File file : f.listFiles()) {
                if (file.isDirectory()) {
                    System.out.println("dir--> " + file.getName());
                    count++;
                }
            }
            LogHelper.makeLog("The number folders in: " + folder + " is: " + count);
        }
    }

    private static void countFilesInDir() {
        String folder = "E:/Desarrollo/Dunkelheit/FileSystem/users/user00/00/00/01/profile";
        File f = new File(folder);
        if (f.isDirectory()) {
            int count = 0;
            for (File file : f.listFiles()) {
                if (file.isFile()) {
                    count++;
                }
            }
            LogHelper.makeLog("The number of files in folder: " + folder + " are: " + count);
            listFiles(f);
        }
    }

    public static long listFiles(File file) {

        //Store the total size of all files
        long size = 0;

        if (file.isDirectory()) {

            //All files and subdirectories
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                //Recursive call
                String ab = files[i].getName();
                LogHelper.makeLog(ab);
            }

        }
        return size;

    }

    private static void generateUniqueFileName() {
        String a = System.getProperty("file.separator");
        String fileName = "users" + a + "user00" + a + "00" + a + "00" + a + "01" + a + "" + a + "profile" + a + "DSCF0034.jpg";
        fileName = "sdfsfsdf.jpg";
        LogHelper.makeLog("before: " + fileName);
        fileName = getUniqueFileName(fileName);
        LogHelper.makeLog("after: " + fileName);
    }

    public static String getUniqueFileName(String fileName) {
        FormatHelper formatHelper = new FormatHelper();
        String extension = null;

        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.indexOf("."));
        } else {
            extension = "";
        }

        return formatHelper.format_yyyyMMddHHmmss(new Date()) + RandomStringUtilsExt.randomAlphanumeric(4) + extension;
    }

    /**
     * creates a range of file systems given the start en en number.
     */
    private static void createUserFileSystem() {
        for (int i = 2; i < 15; i++) {
            try {
                createUserFileSystem(i);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void createUserFileSystem(int userId) throws IOException {
        StringBuilder sb = new StringBuilder();
        String fileSeparator = System.getProperty("file.separator");
        String userPath = generateUserPath(userId);

        sb.append(FILE_SYSTEM);
        sb.append(fileSeparator);
        sb.append(userPath);
        sb.append(fileSeparator);

        userPath = sb.toString();
        createDir(userPath + "profile");
        createDir(userPath + "galleries");
        createDir(userPath + "stuff");

        LogHelper.makeLog("For " + userId + " the dir will be: " + userPath);
    }

    /**
     * Based on the Users ID which is an autonumeric secuencial number
     * it will give a unique structured path so he can put his fucking shit in it.
     *
     * example: for an Id of 9123456 --> User09\12\34\56\
     *          for an Id of 1       --> User00\00\00\01\
     * @param user Id
     * @return generated Path
     */
    private static String generateUserPath(Integer userId) {
        StringBuilder sb = new StringBuilder();
        String sNumber = userId.toString();
        int size = sNumber.length();
        String fileSeparator = System.getProperty("file.separator");
        String constante1 = "users";
        String constante2 = "user";
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
        String path = sb.toString();
        return path;
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

    private static void decomposeDate() throws NumberFormatException {
        Date a = convertStringToDate();
        Calendar c = Calendar.getInstance();
        c.setTime(a);
        String year = "" + c.get(Calendar.YEAR);
        String month = "" + c.get(Calendar.MONTH);
        String day = "" + c.get(Calendar.DAY_OF_MONTH);
        LogHelper.makeLog("Year: " + year + ", month: " + month + ", day: " + day);
    }

    private static Date convertStringToDate() throws NumberFormatException {
        Date returnVale = null;
        String yyyy = "1981";
        String MM = "07";
        String dd = "05";
        String yyyy1 = "2010";
        String MM1 = "09";
        String dd1 = "31";
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        Calendar c = new GregorianCalendar(Integer.parseInt(yyyy), Integer.parseInt(MM) - 1, Integer.parseInt(dd));
        Calendar c2 = new GregorianCalendar(Integer.parseInt(yyyy1), Integer.parseInt(MM1) - 1, Integer.parseInt(dd1));
        try {
            Date myBirthday = df.parse(dd + MM + yyyy);
            LogHelper.makeLog("Fecha1: " + myBirthday.toString());
            LogHelper.makeLog("Fecha2: " + c.getTime().toString());
            LogHelper.makeLog("Fecha3: " + c2.getTime().toString());

            returnVale = c.getTime();
        } catch (Exception ex) {
            LogHelper.makeLog(ex.getMessage(), ex);
        }
        return returnVale;
    }

    private static void compareTest1() {
        Integer profile = 10;
        LogHelper.makeLog("Is profile: " + profile + ", an administrator: " + isAdmin1(profile));
    }

    private static void DeleteHtmlInvalidTags() {
        ExternalResourceBlocker e = ExternalResourceBlocker.getInstance();
        String x = "<div class='comment'> " + "	<p>Sort of unrelated: one of the funniest impressions I have ever seen is Dawn French doing &#8220;Catherine Zeta Spartacus Jones&#8221; as an entitled, smug, nouveau riche Welsh broad.  Trust me&#8230;You Tube it!</p> " + " <p>Oh, and I agree with Busy Body.  Michael has that old man smell&#8230;.";
        String y = "<div><img src='http://www.gravatar.com/avatar/baa3210e82ebfb3185fbe92ecdd2fe90?&amp;r=PG&amp;d=http%3A%2F%2Fwww.celebitchy.com%2Fwp-content%2Fthemes%2Fdefault%2Fimages%2Fdefaultgrav.jpg' alt='MaiGirl' class='gravatar avatar' /></div>";
        String z = "<iframe width='560' height='315' src='http://www.youtube.com/embed/jBFC8xWHH9g?rel=0' frameborder='0' allowfullscreen></iframe>";
        String a = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String b = "<script type='text/javascript'> document.write('Hello World!') </script>";
        String c = "<h1><sup><Font class='Apple-style-span' color='#ff0000'>PRUBEA</font></sup></h1>";
        x = x + y + z + a + b + c;
        LogHelper.makeLog("Entrada: " + x);


        String[] xxx = new String[]{"font", "color"};
        Whitelist w = Whitelist.relaxed().addTags(xxx);

        LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
        LogHelper.makeLog("-- basicWithImages --> " + Jsoup.clean(x, Whitelist.basicWithImages()));
        LogHelper.makeLog("-- basic -------> " + Jsoup.clean(x, Whitelist.basic()));
        LogHelper.makeLog("-- simpleText --> " + Jsoup.clean(x, Whitelist.simpleText()));
        LogHelper.makeLog("-- none --------> " + Jsoup.clean(x, Whitelist.none()));
        LogHelper.makeLog("-- relaxed -----> " + Jsoup.clean(x, w));


        if (e.tieneTagsHtmlInvalidos(x)) {
//            x = e.eliminarTagsHtmlInvalidos(x);

            x = e.deleteAllHtmlTags(x);
            LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
            LogHelper.makeLog("Salida: " + x);
        }
    }

    private static void TestStringFormat() {
        String x = "El usuario  con email: %1$s , ya se encuentra en el sistema";
        Object[] a = {"perro", "gato"};
        String answer = String.format(x, a);
        LogHelper.makeLog(answer);
    }

    private static void mkdirs(File file) throws IOException {
        if (file.exists() && !file.isFile()) {
            throw new IOException("File " + file.getPath() + " is actually not a file.");
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Creating directories " + parentFile.getPath() + " failed.");
        }
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
    public static File createDir(String fileSystemPath)
            throws IOException {

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
            throw new IOException("Unable to create the user directory because it is already created " + dir);
        }

        return dir;
    }

    public static boolean isDirectoyAGallery(String mensaje) {
        boolean respuesta = false;

        Pattern datePattern = Pattern.compile("[\\\\/]*(galleries)[\\\\/]+(gallery)", Pattern.CASE_INSENSITIVE);



        Matcher matcher = datePattern.matcher(mensaje);
        if (matcher.find()) {
            respuesta = true; // Its a gallery!!!
        }

        return respuesta;
    }

    private static String generarPasswordAleatorio() {
        StringBuilder s = new StringBuilder();
        int c = 'A';
        for (int i = 0; i < 4; i++) {
            c = 'a' + (int) (Math.random() * 26);
            s.append((char) c);
        }
        for (int i = 0; i < 4; i++) {
            c = '0' + (int) (Math.random() * 10);
            s.append((char) c);
        }
        for (int i = 0; i < 4; i++) {
            c = 'A' + (int) (Math.random() * 26);
            s.append((char) c);
        }

        return s.toString();
    }

    private static void doesFileHaveValidImageExtention() {
        doesFileHaveValidImageExtention1("anImage.jpg");
        doesFileHaveValidImageExtention1("中国/中华.jpg");
        doesFileHaveValidImageExtention1("an-Image.jpg");
        doesFileHaveValidImageExtention1("E:/Desarrollo/Dunkelheit/FileSystem/users/user00/00/00/01/wall/images001/../../../|-20111119140327v4ft.jpg");
        doesFileHaveValidImageExtention1("1+1.jpg");
        doesFileHaveValidImageExtention1("1 1.jpg");
        doesFileHaveValidImageExtention1(".jpg");
        doesFileHaveValidImageExtention1(" .jpg");
        doesFileHaveValidImageExtention1(".jpg");


//E:/Desarrollo/Dunkelheit/FileSystem/users/user00/00/00/01/wall/images001/20111120125655Aull.jpg        

    }

    private static void doesFileHaveValidImageExtention1(String image) {
        boolean test = ImageValidator.validate(image);

        if (test) {
            LogHelper.makeLog("YESS Image: " + image);
        } else {
            LogHelper.makeLog("NOOO Passed: " + image);
        }

    }

    private static void testSearchOrderBy() {
        String orderBy = " order by e.eventDate  desc  ";

        if (orderBy.trim().toLowerCase().endsWith("desc")) {
            System.out.println("desc");
        } else if (orderBy.trim().endsWith("asc")) {

            System.out.println("asc");
        }
    }

    //http://www.devdaily.com/blog/post/java/remove-non-alphanumeric-characters-java-string
    private static void removeNonAlphanumericCharacters() {

        String s = "yo-dude: like 69, ''\n ''--  ... []{}this is a string ".trim();
        System.out.println(s);
        s = s.replaceAll("[^a-zA-Z0-9]", " ");
        System.out.println(s);
        s = s.replaceAll("\\s{2,}", " ");
        System.out.println(s);
        s = s.replaceAll(" ", "-");
        System.out.println(s);
    }

    private static void validateNumber() {
        String number = "33334 \n";
        if (numberPattern.matcher(number).matches()) {
            LogHelper.makeLog("YESS number: " + number);
        } else {
            LogHelper.makeLog("NOOO number: " + number);
        }

    }

    public static void getMemoryStats() {
        //Declare the size
        int megaBytes = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        System.out.println(" ------| JVM Memory Utilization Statistics (in MB) |-------");

        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / megaBytes;
        long maxMemory = runtime.maxMemory() / megaBytes;
        //Print used memory
        System.out.println("Used Memory: " + usedMemory);

        //Print free memory
        System.out.println("Free Memory: " + runtime.freeMemory() / megaBytes);

        //Print total available memory
        System.out.println("Total Memory: " + runtime.totalMemory() / megaBytes);

        //Print Maximum available memory
        System.out.println("Max Memory: " + maxMemory);

        //Print Percent
        System.out.println("Percent: "
                + usedMemory * 100 / maxMemory);


    }

    private static void getClientIpAddressXForwardedFor() {
        String remoteAddr = "127.0.0.1, 192.168.1.102";
        int idx = remoteAddr.indexOf(',');
        if (idx > -1) {
            remoteAddr = remoteAddr.substring(0, idx);
        }

        System.out.println("--> " + remoteAddr);

    }

    private static String getYoutubeId(String input) {

        Pattern p = Pattern.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*", Pattern.CASE_INSENSITIVE);
        Pattern p1 = Pattern.compile("^[^v]+v=(.{11}).*", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile(".*youtu.be/(.{11}).*", Pattern.CASE_INSENSITIVE);

        //      Pattern p = Pattern.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*", Pattern.CASE_INSENSITIVE);
        //(?<=v=)[a-zA-Z0-9-]+(?=&)|(?<=[0-9]/)[^&\n]+|(?<=v=)[^&\n]+

        Matcher m = p1.matcher(input);

        if (m.matches()) {
            return m.group(1);
        } else {
            m = p2.matcher(input);
            if (m.matches()) {
                return m.group(1);
            }
        }

        return "no match found";

    }

    private static void testGetYouTubeVideoId() {
        Pattern pat = Pattern.compile("(?<=v=)[a-zA-Z0-9-]+(?=&)|(?<=[0-9]/)[^&\n]+|(?<=v=)[^&\n]+", Pattern.CASE_INSENSITIVE);

        String link = "http://youtube.com/watch?v=videoid";
        link = "http://www.youtube.com/user/Scobleizer#p/u/1/1p3vcRhsYGo";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "https://www.youtube.com/watch?v=cKZDdG9FTKY&feature=channel";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://www.youtube.com/watch?v=yZ-K7nCVnBI&playnext_from=TL&videos=osPknwzXEas&feature=sub";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://www.youtube.com/ytscreeningroom?v=NRHVzbJVx8I";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://www.youtube.com/watch?v=C7ZrU2fwKME&feature=grec_index";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://www.youtube.com/watch?v=HM9BCnqTNlw&feature=grec_index";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://youtu.be/HM9BCnqTNlw";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "http://www.youtube.com/watch?v=3HDRUiu-DcM&feature=grec_index";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "youtu.be/HM9BCnqTNlw";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);
        link = "youtube.com/watch?v=3HDRUiu-DcM&feature=grec_index";
        System.out.println("id: " + getYoutubeId(link) + ", link: " + link);




    }

    private static void validateYouTubeEmbededVideo() {


        ExternalResourceBlocker e = ExternalResourceBlocker.getInstance();
        String x = "<iframe width='425' height='349' src='http://www.youtube.com/embed/z99EHyG2jQA' frameborder='0' allowfullscreen></iframe>";
        String y = "<div><img src='http://www.gravatar.com/avatar/baa3210e82ebfb3185fbe92ecdd2fe90?&amp;r=PG&amp;d=http%3A%2F%2Fwww.celebitchy.com%2Fwp-content%2Fthemes%2Fdefault%2Fimages%2Fdefaultgrav.jpg' alt='MaiGirl' class='gravatar avatar' /></div>";
        x = x + y;
        LogHelper.makeLog("Entrada: " + x);
        LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
        if (e.tieneTagsHtmlInvalidos(x)) {
//            x = e.eliminarTagsHtmlInvalidos(x);

            x = e.deleteAllHtmlTags(x);
            LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
            LogHelper.makeLog("Salida: " + x);
        }
    }

    private static void getDomainFromUrl() {


        String[] urls = new String[]{
            "https://foo.com/bar",
            "http://www.foo.com#bar",
            "http://bar.foo.com"
        };

        for (String url : urls) {
            System.out.println("url: " + url + ", domain: " + getDomainUrl(url));
        }
    }

    private static String getDomainUrl(String url) {
        String answer = null;
        try {
            URI uri = new URI(url);
            //eg: uri.getHost() will return "www.foo.com"
            Matcher m = urlPartsPattern.matcher(uri.getHost());
            if (m.matches()) {
                answer = m.group(0);
            } else {
                answer = "";
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            answer = "";
        }



        return answer;
    }

    private static void testBuildDate_yyyyMMddWithPadding() {


        Calendar cal = Calendar.getInstance();

        FormatHelper f = new FormatHelper();
        System.out.println("" + f.format_yyyyMMdd(cal.getTime()));


        String sDate = ("" + cal.get(Calendar.YEAR)).concat(DataHelper.colocarCaracteresALaIzquierda('0', 2, "" + cal.get(Calendar.MONTH))).concat(DataHelper.colocarCaracteresALaIzquierda('0', 2, "" + cal.get(Calendar.DAY_OF_MONTH)));
        System.out.println("--> " + sDate);
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
    private static void generateUserId() {
        final long startTime = System.currentTimeMillis();

        String id = null;

        Calendar cal = Calendar.getInstance();
        FormatHelper f = new FormatHelper();

        id = RandomStringUtilsExt.randomNumeric(2).concat(f.format_yyMMddHHmmssSSS.format(cal.getTime()));
        System.out.println("id: " + id + ", " + cal.getTime().toString());

        final long duration = System.currentTimeMillis() - startTime;
        LogHelper.makeLog("Time: " + duration);

    }

    private static void testRemoveBackSlashesFromFilePath() {
        String x = "\\abc\\def\\\\\\g";
        System.out.println(x + "--> " + removeBackSlashesFromFilePath(x));
        System.out.println(x + "--> " + removeBackSlashesFromFilePath(x));
    }

    public static String removeBackSlashesFromFilePath(String mensaje) {
        return mensaje.replaceAll("\\\\+", "/");
    }

    private static void junk() {
        Date d = new Date();
        long t = d.getTime();
        System.out.println("t/hour: " + (t / 60000));

        System.out.println("");
        String x = "Gonz\u0026aacute;lez";
        System.out.println("name: " + x);
        System.out.println("name: " + new String(x));
        System.out.println("name: " + new String(x.getBytes(), Charset.forName("ISO-8859-1")));

        System.out.println("");


//            String clean = StringEscapeUtils.unescapeHtml4(dirty).replaceAll("[^\\x20-\\x7e]", "");

        try {
            byte[] utf8 = new String(x.getBytes(), "ISO-8859-1").getBytes("UTF-8");
            System.out.println("name: " + utf8.toString());
            System.out.println("name: " + new String(utf8));

            // Convert from Unicode to UTF-8
            String string = "abc\u5639\u563b";
            utf8 = x.getBytes("ISO-8859-1");

            // Convert from UTF-8 to Unicode
            string = new String(utf8, "ISO-8859-1");
            System.out.println("string: " + string);
        } catch (UnsupportedEncodingException e) {
        }


// Create the encoder and decoder for ISO-8859-1
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        try {
            // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
            // The new ByteBuffer is ready to be read.
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(x));

            // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
            // The new ByteBuffer is ready to be read
            CharBuffer cbuf = decoder.decode(bbuf);
            String s = cbuf.toString();
            System.out.println("string2: " + s);
        } catch (CharacterCodingException e) {
        }



        //js 21950112
        //j  21950113
    }

    public static String getYouTubeId(String youTubeUrl) {

        if (youTubeUrl != null) {

            Matcher m = youTubePattern1.matcher(youTubeUrl); //for patterns that have www.youtube.com/watch?v=cLnTjHywE54

            if (m.matches()) {
                return m.group(1);
            } else {
                //So Im going to try another pattern.
                m = youTubePattern2.matcher(youTubeUrl); //for patterns that have youtu.be/cLnTjHywE54
                if (m.matches()) {
                    return m.group(1);
                }
            }
        }

        return null;

    }

    private static void testTransformComment() {
        System.out.println("");
        String a = "<div class='comment'> "
                + "	<p>Sort of unrelated: one of the funniest impressions I have ever seen is Dawn French doing &#8220;Catherine Zeta Spartacus Jones&#8221; as an entitled, smug, nouveau riche Welsh broad.  Trust me&#8230;You Tube it!</p> "
                + " <p>Oh, and I agree with Busy Body.  Michael has that old man smell&#8230;.</p>"
                + "<a href='http://www.youtube.com/watch?v=Qt2mbGP6vFI'>http://www.youtube.com/watch?v=Qt2mbGP6vFI</a>"
                + "<a href='http://www.youtube.com/watch?v=tI_sv5uswoY&feature=related'>Pink Floyd</a>"
                + "<link rel='alternate' media='handheld' href='http://www.facebook.com/?ref=logo' /> "
                + " <iframe width='560' height='315' src='http://www.youtube.com/embed/1CP4px_gu5M?rel=0' frameborder='0' allowfullscreen></iframe> "
                + " &lt;iframe width='560' height='315' src='http://www.youtube.com/embed/1CP4px_gu5M?rel=0' frameborder='0' allowfullscreen&gt;&lt;/iframe&gt; ";

        
        System.out.println("--a-->" + a);

        System.out.println("");
        String x = Jsoup.clean(a, Whitelist.basicWithImages());
        System.out.println("--basicWithImages-->" + x);
        
        System.out.println("");
        String y = Jsoup.clean(a, Whitelist.relaxed());
        System.out.println("--relaxed-->" + y);

        Document doc = Jsoup.parse(x);
        Elements links = doc.getElementsByTag("a");
        System.out.println("");
        System.out.println("links: " + links.size());

        List<PairDTO> l = new ArrayList<PairDTO>();
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            String outerHtml = link.outerHtml();
            link.appendText("------");
            System.out.println("linkHref: " + linkHref + ", linkText: " + linkText + ", outer" + outerHtml);


            String id = YouTubeClient.getYouTubeId(linkHref);
            if (id != null) {
                //It is a YouTube url
                l.add(new PairDTO(linkHref, outerHtml, id));
            }


        }

        System.out.println("");
        int cont = 1;
        YouTubeClient client = new YouTubeClient();
        for (Iterator<PairDTO> it = l.iterator(); it.hasNext();) {
            PairDTO pairDTO = it.next();
            try {
                //            x = x.replace(pairDTO.getTwo(), "#_youtube_" + cont + "#");

                YouTubeVideoDTO dto = client.retrieveVideo(pairDTO.getThree());

                String two = auxConvertOne(dto.getThumbnail1(), dto.getTitle());
                x = x.replace(pairDTO.getTwo(), two);

            } catch (Exception ex) {
                LogHelper.makeLog(ex);
            }



            cont++;
        }




        System.out.println("");
        System.out.println("--x2-->" + x);


    }

    private static String auxConvertOne(String imgLink, String title) {
        String x = " <li class='ol1'> "
                + "     <h3> "
                + " 		<a href='/news/health-15871905' class='play'> "
                + " 			<span class='gvl3-icon-wrapper'> "
                + " 				<span class='gvl3-icon gvl3-icon-invert-watch'> Watch</span> "
                + " 			</span> "
                + " 			" + title + " "
                + " 			<img src='" + imgLink + "' /> "
                + " 		</a> "
                + " 	</h3> "
                + " </li>";


        return x;
    }

    private static boolean isLinkYouTube(String linkHref) {
        String id = getYouTubeId(linkHref);
        if (id == null) {
            return false;
        }

        return true;
    }
    
    private static Date addHour(Date date1, int hour) {

        GregorianCalendar date = new GregorianCalendar();
        date.setTime(date1);
        date.add(Calendar.HOUR, hour);

        return date.getTime();
    }    
    
//    private static void DeleteHtmlInvalidTags() {
//        ExternalResourceBlocker e = ExternalResourceBlocker.getInstance();
//        String x = "<div class='comment'> " + "	<p>Sort of unrelated: one of the funniest impressions I have ever seen is Dawn French doing &#8220;Catherine Zeta Spartacus Jones&#8221; as an entitled, smug, nouveau riche Welsh broad.  Trust me&#8230;You Tube it!</p> " + " <p>Oh, and I agree with Busy Body.  Michael has that old man smell&#8230;.";
//        String y = "<div><img src='http://www.gravatar.com/avatar/baa3210e82ebfb3185fbe92ecdd2fe90?&amp;r=PG&amp;d=http%3A%2F%2Fwww.celebitchy.com%2Fwp-content%2Fthemes%2Fdefault%2Fimages%2Fdefaultgrav.jpg' alt='MaiGirl' class='gravatar avatar' /></div>";
//        String z = "<iframe width='560' height='315' src='http://www.youtube.com/embed/jBFC8xWHH9g?rel=0' frameborder='0' allowfullscreen></iframe>";
//        String a = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
//        String b = "<script type='text/javascript'> document.write('Hello World!') </script>";
//        String c = "<h1><sup><Font class='Apple-style-span' color='#ff0000'>PRUBEA</font></sup></h1>";
//        x = x + y + z + a + b + c;
//        LogHelper.makeLog("Entrada: " + x);
//
//
//        String[] xxx = new String[]{"font", "color"};
//        Whitelist w = Whitelist.relaxed().addTags(xxx);
//
//        LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
//        LogHelper.makeLog("-- basicWithImages --> " + Jsoup.clean(x, Whitelist.basicWithImages()));
//        LogHelper.makeLog("-- basic -------> " + Jsoup.clean(x, Whitelist.basic()));
//        LogHelper.makeLog("-- simpleText --> " + Jsoup.clean(x, Whitelist.simpleText()));
//        LogHelper.makeLog("-- none --------> " + Jsoup.clean(x, Whitelist.none()));
//        LogHelper.makeLog("-- relaxed -----> " + Jsoup.clean(x, w));
//
//
//        if (e.tieneTagsHtmlInvalidos(x)) {
////            x = e.eliminarTagsHtmlInvalidos(x);
//
//            x = e.deleteAllHtmlTags(x);
//            LogHelper.makeLog("Tiene tieneTagsHtmlInvalidos: " + e.tieneTagsHtmlInvalidos(x));
//            LogHelper.makeLog("Salida: " + x);
//        }
//    }
}
