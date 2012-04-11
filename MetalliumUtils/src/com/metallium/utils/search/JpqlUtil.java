/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.search;

import com.metallium.utils.utils.UtilHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.persistence.Query;

/** 
 *
 * @author Ruben
 */
public class JpqlUtil {

    public JpqlUtil() {
    }

    public static String buildJpqlSentence(Collection<String> filters, String sqlCondition, String order, String addOnExtraParameters) {
        Iterator<String> it = filters.iterator();
        String where = " WHERE ";

        if (UtilHelper.isStringEmpty(sqlCondition)) {
            sqlCondition = SqlCondition.AND; //By Default, the SQL condition is 'AND' if no SQL condition is specified. The SqlCondition could also have been 'OR'.
        }

        StringBuilder anwser = new StringBuilder(filters.size() * 16);
        anwser.append(where);

        if (!UtilHelper.isStringEmpty(addOnExtraParameters)) {
            anwser.append(addOnExtraParameters); //This is just in special cases like for example in the ProfileFriends.xhtml friends query search.
        }

        boolean firstTime = true;

        while (it.hasNext()) {
            if (firstTime) {
                anwser.append(" ( ").append(it.next());
                firstTime = false;
            } else {
                anwser.append(sqlCondition.concat(it.next()));
            }
        }

        // No filter applied
        if (anwser.toString().equals(where)) {
            return order;
        }

        if (!firstTime){
            anwser.append(" ) "); //I only close this parenthesis if I openned it. And that is if 'firstTime' is false
        }

        anwser.append(order);
        return anwser.toString();
    }

    public static void fillJpqlSentence(Query data, HashMap<String, Object> parameters) {
        // obtain all the Keys and fill out their values
        for (Entry<String, Object> e : parameters.entrySet()) {
            data.setParameter(e.getKey(), e.getValue());
        }
    }

    public static int getNumberOfPages(double totalRegisters, double maxRegisters) {
        return totalRegisters == 0 ? 1 : (int) Math.ceil(totalRegisters / maxRegisters);
    }
}
