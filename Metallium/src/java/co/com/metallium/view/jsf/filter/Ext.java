/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.view.jsf.filter;

import com.metallium.utils.framework.utilities.LogHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 20101214
 * @author Ruben
 */
public class Ext {


    public static void logAccessDenied(String ip, String usuario, String url, String motivo){
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n----------------------------------------------------------------------\r\n");
        sb.append("Access Denied intent ...\r\n");
        sb.append("\tDate  : ");sb.append(new SimpleDateFormat("MMMM dd, yyyy - HH:mm:ss (hh:mm aa)").format(new Date()));sb.append("\r\n");
        sb.append("\tIP     : ");sb.append(ip == null ? "N/A" : ip);sb.append("\r\n");
        sb.append("\tURL    : ");sb.append(url == null ? "N/A" : url);sb.append("\r\n");
        sb.append("\tMotive : ");sb.append(motivo == null ? "N/A" : motivo);sb.append("\r\n");
        sb.append("----------------------------------------------------------------------\r\n");

        LogHelper.makeLog(sb.toString());

    }

}
