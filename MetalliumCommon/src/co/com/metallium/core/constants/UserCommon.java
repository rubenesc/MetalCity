/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.constants;

/**
 *
 * @author Ruben
 */
public class UserCommon {

    //This is to standardize the constants used to build the path to the relitive user file system
    //an example is would be: absolut path + relitive path
    //relitive path would be: FILE_SYSTEM_USER_CONSTANT_1 + FILE_SYSTEM_USER_CONSTANT_2 + generated user id path.
    public static final String FILE_SYSTEM_USER_CONSTANT_1 = "users";
    public static final String FILE_SYSTEM_USER_CONSTANT_2 = "user";
    //This is to standardize the names of the directories in the user's file system directory.
    public static final String FILE_SYSTEM_USER_PROFILE = "profile";
    public static final String FILE_SYSTEM_USER_GALLERIES = "galleries";
    public static final String FILE_SYSTEM_USER_GALLERY = "gallery";
    public static final String FILE_SYSTEM_USER_THUMBNAILS = "thumbnails";
    public static final String FILE_SYSTEM_USER_STUFF = "stuff";
    public static final String FILE_SYSTEM_USER_WALL = "wall";

    //These are to know which section of the profile the user is editing
    public static final String EDIT_PROFILE_PICTURE = "profilePicture";
    public static final String EDIT_PERSONAL = "personal";
    public static final String EDIT_FAVORITE = "favorite";
    public static final String EDIT_OPINION = "opinion";
    public static final String EDIT_WALL_POST_ADD = "addpost";
    public static final String EDIT_WALL_POST_EDIT = "editpost";

    public static final String EDIT_PROFILE_CONFIG = "config";
    public static final String EDIT_USERNAME = "username";
    public static final String EDIT_PASSWORD = "password";
    public static final String EDIT_EMAIL = "email";
    public static final String EDIT_DELETE_PROFILE = "delete";
    public static final String PROFILE_PIC_TEMP= "profileTemp";
    public static final String PROFILE_PIC_LARGE = "profileL";
    public static final String PROFILE_PIC_MED = "profileM";
    public static final String PROFILE_PIC_SMALL = "profileS";
    //Actions that I can do to my friends
    public static final String FRIEND_ADD = "__add_friend__";
    public static final String FRIEND_DELETE = "__delete_friend__";
    public static final String FRIEND_BLOCK = "__block_friend__";
    public static final String FRIEND_REPORT = "__report_friend__";
    public static final String FRIEND_WRITE_ON_WALL = "__write_on_wall__";
    public static final String FRIEND_VIEW_FRIEND_LIST = "__view_friend_list__";





}
