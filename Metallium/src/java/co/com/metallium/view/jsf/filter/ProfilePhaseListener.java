/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.filter;

import co.com.metallium.core.entity.User;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.user.LoginUserManagedBean;
import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.IOException;
import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * This Phase Listener is meant to fix one little fucking issue I have.
 * When a user authenticates he pushes a "Login" button, which triggers a POST
 * request. If the user authenticates correctly I send him to his profile page,
 * but the profile page can only be accessed though a GET request, not POST, because
 * in the GET request has to go the ID of the client who authenticated.
 *
 * So after messing around a thousand hours I came with this PhaseListener that
 * does the trick.
 *
 * It converts a certain POST request in a GET Request.
 * Once again thanks to
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/03/post-redirect-get-pattern.html
 *
 * I just trimmed it and adapted it to my needs.
 * 20110205
 * @author Ruben
 * 
 */
public class ProfilePhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {

        if (true) {
            //I dont need this Anymore. I just gave up.
            //So when I log in I go to the Index page.

            Boolean isAuth = (Boolean) event.getFacesContext().getExternalContext().getSessionMap().get("__PHASE_AUTH__");

            if (isAuth != null && isAuth) {


                ELContext elContext = FacesContext.getCurrentInstance().getELContext();
                LoginUserManagedBean neededBean = (LoginUserManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "loginUserManagedBean");

                User user = (User) event.getFacesContext().getExternalContext().getSessionMap().get("__AUTHENTICATED_USER__");
                if (user != null) {
                    neededBean.logIn(user);
                }

                event.getFacesContext().getExternalContext().getSessionMap().put("__PHASE_AUTH__", null);
            }


            return;
        }




        // Prepare.
        FacesContext facesContext = event.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();


        String requestUri = JsfUtil.getRequestUri();
        String facesRequestURI = JsfUtil.getFacesRequestUri();
        String sid = request.getParameter("id");


        if ("POST".equalsIgnoreCase(request.getMethod())) {

            if (doesRequestNeedToBeRedirected(requestUri, facesRequestURI)) {

                Integer id = getAuthenticatedUserId();
                if (id != null) {
                    // Redirect POST request to GET request.
                    redirectToProfile(facesContext, id);
                } else {
                    //I send the request to the Index or startup Page that is in defined the web.xml tag 'welcome-file'
                    redirect(facesContext, request.getContextPath());
                }
            }

            if (doesRequestNeedToBeRedirected3(requestUri, facesRequestURI)) {

                if (UtilHelper.isStringEmpty(sid)) {

                    Integer id = getAuthenticatedUserId();
                    if (id != null) {
                        // Redirect POST request to GET request.
                        redirectToProfile(facesContext, id);
                    } else {
                        //I send the request to the Index or startup Page that is in defined the web.xml tag 'welcome-file'
                        redirect(facesContext, request.getContextPath());
                    }




                }


            }



//            else if (doesRequestNeedToBeRedirected2(requestUri, facesRequestURI)) {
//
//                if (!isUserAuthenticated()) {
//                    //I send the request to the Index or startup Page that is in defined the web.xml tag 'welcome-file'
//                    redirect(facesContext, request.getContextPath());
//                }
//
//            }

        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Integer id = getAuthenticatedUserId();

        }


    }

    // Helpers ------------------------------------------------------------------------------------
    /**
     *
     * There are a number of situations where I have to redirect a request to somewhere else.
     * Example1: I have to make sure that user is coming from the "index" page and is going to
     *          the Profile Page, is because he just Logged in, So I HAVE To cover the 'post' to 'get'
     * Example2: I am in the editprofile page and am going to the profile page. 
     * Example3: For some reason I just redeployed the application and I click the for the home page
     *          well, I for some reason go from the profile to the profile, so its crazy but there it is.
     *
     * @param requestUri
     * @param facesRequestURI
     * @return
     */
    private boolean doesRequestNeedToBeRedirected(String requestUri, String facesRequestURI) {

        if ((isRequestUriTheIndexPage(requestUri)
                || isRequestUriTheEditProfilePage(requestUri))
                && "/portal/user/profile/profile.xhtml".equalsIgnoreCase(facesRequestURI)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean doesRequestNeedToBeRedirected3(String requestUri, String facesRequestURI) {

        if ("/portal/user/profile/profile.xhtml".equalsIgnoreCase(facesRequestURI)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * There are a number of situations where I have to redirect a request to somewhere else.
     *
     * Example3: For some reason I just redeployed the application and I click the for the home page
     *          well, I for some reason go from the profile to the profile, so its crazy but there it is.
     *
     */
    private boolean doesRequestNeedToBeRedirected2(String requestUri, String facesRequestURI) {

        if (isRequestUriTheProfilePage(requestUri)
                && "/portal/user/profile/profile.xhtml".equalsIgnoreCase(facesRequestURI)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Invoke a redirect to the same URL as the current action URL.
     * @param facesContext The involved facescontext.
     */
    private static void redirectToProfile(FacesContext facesContext, Integer id) {

        // Obtain the action URL of the current view.
        String url = facesContext.getApplication().getViewHandler().getActionURL(
                facesContext, facesContext.getViewRoot().getViewId()).concat("?id=" + id);


        redirect(facesContext, url);

    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            // Invoke a redirect to the action URL.
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            // Uhh, something went seriously wrong.
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    /**
     * Tells me if a String matches the Index page pattern which is
     * a pattern that has at least this "/index."
     * Its just for something simple its not really meant to be bullet proof
     *
     *      /metallium/index.jsf --> Yes
     *      /metallium/indexjsf --> no
     *
     * @param uri
     * @return
     */
    private boolean isRequestUriTheIndexPage(String uri) {

        if (MetUtilHelper.indexPagePattern.matcher(uri).matches()) {
            return true;
        }
        return false;
    }

    private boolean isRequestUriTheEditProfilePage(String uri) {

        if (MetUtilHelper.editProfilePagePattern.matcher(uri).matches()) {
            return true;
        }
        return false;
    }

    private boolean isRequestUriTheProfilePage(String uri) {

        if (MetUtilHelper.profilePagePattern.matcher(uri).matches()) {
            return true;
        }
        return false;
    }

    private boolean isUserAuthenticated() {

        try {
            return (Boolean) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED);
        } catch (Exception e) {
            //I dont care
        }

        return false;
    }

    private Integer getAuthenticatedUserId() {

        Integer id = null;

        try {
            boolean authenticated = (Boolean) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED);

            if (authenticated) {
                id = ((User) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED_USER)).getId();
            }
        } catch (Exception e) {
            //I dont care
        }


        return id;

    }

    public PhaseId getPhaseId() {
//        return PhaseId.RENDER_RESPONSE;
        return PhaseId.RESTORE_VIEW;
    }
}
