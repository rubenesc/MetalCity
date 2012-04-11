
import co.com.metallium.core.constants.MetConfiguration;
import co.com.metallium.core.constants.state.ForumCategoryStateEnum;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.service.dto.search.GeneralSearchDTO;
import co.com.metallium.core.service.dto.search.NewsSearchDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.search.SearchBaseDTO;
import com.metallium.utils.utils.UtilHelper;
import java.lang.reflect.Field;
import java.util.Date;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruben
 */
public class MainTest {

    public static void main(String[] args) {
        //     reflectionTest1();
//        reflectionTest2();
//        chooseMethodTest();
       // getTimeLeftMessage();

        testEnum();

    }

    /**
     * Knowing the name of the field and class object, I want
     * to SET by reflection the value of the field in the class.
     *
     */
    private static void reflectionTest1() {


        String id = "MAX_NUMBER_ROWS_IN_SEARCH_NEWS";
        String value = "Boston";
        String claseName = "MetConfiguration";
        MetConfiguration.DEFAULT_CITY = "Boston";

        try {
            MetConfiguration objInstante = new MetConfiguration();
            Class cl = MetConfiguration.class;// Class.forName("co.com.metallium.core.constants.MetConfiguration");

            Field field = cl.getField(id);
            Class type = field.getType();

            if (type == String.class) {
                field.set(objInstante, value);
            } else if (type == int.class) {
                field.set(objInstante, Integer.parseInt("666"));
            }
            LogHelper.makeLog("WTF is this --> " + field.getName() + ", type: " + field.getType().getSimpleName());

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }



        LogHelper.makeLog("--> " + MetConfiguration.MAX_NUMBER_ROWS_IN_SEARCH_NEWS);

    }

    /**
     * Knowing the name of the field and class object, I want
     * to KNOW by reflection the value of the field in the class.
     */
    private static void reflectionTest2() {

        String value = UtilHelper.reflectionGetStringPropertyFromClass(User.class, "entitySearchQueryFragment");
        LogHelper.makeLog("Value: " + value);

    }

    private static void chooseMethodTest() {

        SearchBaseDTO searchDTO;
        GeneralSearchDTO a = new GeneralSearchDTO();
        NewsSearchDTO b = new NewsSearchDTO();
        chooseMethod(b);
    }

    private static void chooseMethod(SearchBaseDTO obj) {

        if (obj instanceof GeneralSearchDTO) {
            method((GeneralSearchDTO) obj);
        } else if (obj instanceof NewsSearchDTO) {
            method((NewsSearchDTO) obj);
        } else {
            LogHelper.makeLog("Nada !!! ");
        }

    }

    public static void method(GeneralSearchDTO param) {
        LogHelper.makeLog("Method GeneralSearch: " + param.toString());
    }

    public static void method(NewsSearchDTO param) {
        LogHelper.makeLog("Method NewsSearch: " + param.toString());
    }

    private static String getTimeLeftMessage() {
        String answer = null;
        Date futureDate = UtilHelper.addMinutesToDate(190);

        Long x = UtilHelper.dateDifferenceInMinutes(futureDate);

        if (x == null) {
            x = Long.parseLong("666"); // jajajajaajaja This means Go fuck himself 
        }

        answer = "You are banned for some reason I dont know. Come back in " + x + " minutes.";

        LogHelper.makeLog(answer);
        return answer;
    }

    private static void testEnum() {
        System.out.println("Test Enum");
        ForumCategoryStateEnum en = ForumCategoryStateEnum.ACTIVE;
        
        System.out.println("value: " + en.getState());
        
         en = ForumCategoryStateEnum.DELETED;
        
        System.out.println("value: " + en.getState());
        
    }
}
