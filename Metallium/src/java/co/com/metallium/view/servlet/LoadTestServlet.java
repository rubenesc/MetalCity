/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.servlet;

import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.UserException;
import co.com.metallium.core.service.UserService;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.TimeWatch;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ruben
 */
@WebServlet(name = "LoadTestServlet", urlPatterns = {"/user/load"})
public class LoadTestServlet extends HttpServlet {

    @EJB
    public UserService userService;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String accion = request.getParameter("accion");

            if ("cf".equalsIgnoreCase(accion)) {
                String sUserId = request.getParameter("id");
                String sStart = request.getParameter("start");
                String sTotal = request.getParameter("total");
                createFriends(sUserId, sStart, sTotal, out);
            } else if ("cfm".equalsIgnoreCase(accion)) {
                String sUserId = request.getParameter("id");
                String tu = request.getParameter("tu");
                String sStart = request.getParameter("start");//
                String sTotal = request.getParameter("total");

                Integer uid1 = Integer.valueOf(sUserId);
                Integer totalUsers = Integer.valueOf(tu);
                Integer uid2 = uid1 + totalUsers;

                List<Integer> l = userService.findUserIdByRange(uid1, uid2);

                System.out.println("");
                System.out.println("total users: " + l.size());
                for (Iterator<Integer> it = l.iterator(); it.hasNext();) {
                    sUserId = it.next().toString();
                    createFriends(sUserId, sStart, sTotal, out);
                }

            } else if ("cu".equalsIgnoreCase(accion)) {
                String sTotal = request.getParameter("total");
                createUser(sTotal, out);
            }


        } catch (Exception e) {
            LogHelper.makeLog(e);
            out.println("error " + e.getMessage());

        } finally {
            out.close();
        }
    }

    //************ this is where all the logic is **********************/
    private void createFriends(String sUserId, String sStart, String sTotal, PrintWriter out) throws UserException {

        Integer userId = Integer.valueOf(sUserId);

        Integer start = 100;
        if (!StringUtils.isEmpty(sStart)) {
            try {
                start = Integer.valueOf(sStart);
            } catch (Exception e) {
                LogHelper.makeLog("start: " + sStart);
            }
        }


        Integer total = 1000;
        if (!StringUtils.isEmpty(sTotal)) {
            try {
                total = Integer.valueOf(sTotal);
            } catch (Exception e) {
                LogHelper.makeLog("total: " + sTotal);
            }
        }

        Integer uid1 = start;
        Integer uid2 = uid1 + total;
        out.println("start: " + start + ", total: " + total + "userId: " + userId);
        TimeWatch watch2 = TimeWatch.start();

        int i = 1;
        List<Integer> userIdList = userService.findUserIdByRange(uid1, uid2);
        for (Iterator<Integer> it = userIdList.iterator(); it.hasNext();) {
            Integer auxUserId = it.next();
            TimeWatch watch = TimeWatch.start();
            try {

                if (userId != auxUserId) {
                    userService.createFriends(userId, auxUserId);
                }

            } catch (Exception e) {
                LogHelper.makeLog(e);
            }
            LogHelper.makeLog("--> friendshipCreated " + i + " user1: " + userId + ", user2: " + auxUserId + " time: " + watch.time());
            i++;
        }

        System.out.println("");
        LogHelper.makeLog("--> registerBomb  start: " + start + " total: " + total + " time: " + watch2.time());
    }

    private void createUser(String sTotal, PrintWriter out) throws UserException {

        Integer total = 500;
        if (!StringUtils.isEmpty(sTotal)) {
            try {
                total = Integer.valueOf(sTotal);
            } catch (Exception e) {
                LogHelper.makeLog("total: " + sTotal);
            }
        }
        Integer start = userService.getLastCreateUserId();
        out.println("start: " + start + ", total: " + total);
        TimeWatch watch2 = TimeWatch.start();
        total = start + total;
        for (int i = start; i <= total; i++) {
            TimeWatch watch = TimeWatch.start();
            User u = new User();
            u.setName("Name" + i);
            u.setEmail("test_" + i + "@gmail.com");
            u.setBirthday(new Date());
            u.setPassword("password" + i);
            u.setSex("M");
            try {
                userService.createUser(u, false); //I go ahead and register the user.
                userService.activateUserAccount(u.getEmail());
            } catch (Exception e) {
                LogHelper.makeLog(e);
            }
            LogHelper.makeLog("--> userActivated " + i + " time: " + watch.time());
        }
        System.out.println("");
        LogHelper.makeLog("--> registerBomb  start: " + start + " total: " + total + " time: " + watch2.time());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
