/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.servlet;

import co.com.metallium.core.constants.GeneralConstants;
import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.service.UserService;
import co.com.metallium.core.util.RegexUtil;
import com.metallium.utils.cipher.EmailCipher;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ruben
 */
@WebServlet(name = "ActivateUserServlet", urlPatterns = {"/user/activate"})
public class ActivateUserServlet extends HttpServlet {

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

        try {
            response.setContentType("text/html;charset=UTF-8");

            String email = request.getParameter(GeneralConstants.ACTIVATION_EMAIL_PARAM);

            boolean isActivated = false;
            if (!UtilHelper.isStringEmpty(email)) {

                try {
                    email = EmailCipher.decode(email);
                } catch (Exception ex) {
                    LogHelper.makeLog("EmailCipher.decode Exception !!! -->  " + ex.getMessage());
                }


                if (RegexUtil.isEmailValid(email)) {
                    isActivated = userService.activateUserAccount(email);
                }
            }

            if (isActivated) {
                forwardRequestTo(Navegation.registrationActive, request, response);
            } else {

                forwardRequestTo(Navegation.indexPage, request, response);
            }

        } catch (Exception ex) {
            LogHelper.makeLog(ex);
        }

    }

    private void forwardRequestTo(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.forward(request, response);
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
