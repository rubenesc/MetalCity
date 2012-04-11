/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.framework.jsf;

import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import javax.faces.FactoryFinder;
import javax.faces.application.*;
import javax.faces.context.*;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

/**
 *
 * @author Ruben
 */
public class FacesUtils {

    private static abstract class FacesContextWrapper extends FacesContext {

        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }

        private FacesContextWrapper() {
        }
    }

    public FacesUtils() {
    }

    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static Application getApplication_() {
        return FacesContext.getCurrentInstance().getApplication();
    }

    public static Object getManagedBean(String beanName) {
        return accessBeanFromFacesContext(beanName, FacesContext.getCurrentInstance());
    }

    public static Object accessBeanFromFacesContext(final String theBeanName, final FacesContext theFacesContext) {
        return theFacesContext.getELContext().getELResolver().getValue(theFacesContext.getELContext(), null, theBeanName);
    }

    public static void resetManagedBean(String beanName) {
//        accessBeanFromFacesContext(beanName, FacesContext.getCurrentInstance());
//        getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
    }

    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    public static void setManagedBeanInRequest(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(beanName, managedBean);
    }

    public static void setValueInApplicationScope(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(key, value);
    }

    public static Object getValueFromApplicationScope(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(key);
    }

    public static void removeValueFromApplicationScope(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().remove(key);
    }

    public static String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        FacesContext.getCurrentInstance().getExternalContext().setRequestCharacterEncoding(encoding);
    }

    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

//    public static Integer evalInt(String el) {
//        if (el == null) {
//            return null;
//        }
//        if (UIComponentTag.isValueReference(el)) {
//            Object value = getElValue(el);
//            if (value == null) {
//                return null;
//            }
//            if (value instanceof Integer) {
//                return (Integer) value;
//            } else {
//                return new Integer(value.toString());
//            }
//        } else {
//            return new Integer(el);
//        }
//    }

    private static Application getApplication() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder.getFactory("javax.faces.application.ApplicationFactory");
        return appFactory.getApplication();
    }

//    private static ValueBinding getValueBinding(String el) {
//        return getApplication().createValueBinding(el);
//    }
//    
    
    private static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

//    private static Object getElValue(String el) {
//        return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
//    }

    private static String getJsfEl(String value) {
        return (new StringBuilder()).append("#{").append(value).append("}").toString();
    }

    public static String getMessageByKey(String key) {
        String messageBundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(messageBundleName);
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public static String getLocalAddress() {
        return ((HttpServletRequest) getExternalContext().getRequest()).getLocalAddr();
    }

    /**
     * This method should return the clients Origin IP address, but in a real life scenario the client
     * will at least pass through my apache web server and further more many other proxies then determining
     * the clients origin ip address can only be obtained in a special header called 'x-forwarded-for'
     *
     * If I fail to get an ip from 'x-forwarded-for' then I have no choice but to get one from the remote
     * address it self which will probably give me the remote address of the las proxy the request came from.
     *
     * @return
     */
    public static String getRemoteAddress() {
        String clientIp = getXForwardedForddress(); //client1, proxy1, proxy2

        if (!UtilHelper.isStringEmpty(clientIp)) {

            return getXForwaredForOrigin(clientIp); //client1

        } else {
            return ((HttpServletRequest) getExternalContext().getRequest()).getRemoteAddr();

        }
    }

    /**
     * The X-Forwarded-For (XFF) HTTP header field is a de facto standard for identifying
     * the originating IP address of a client connecting to a web server through an HTTP
     * proxy or load balancer.
     *
     * The general format of the field is:
     * X-Forwarded-For: client1, proxy1, proxy2
     *
     * return client1, proxy1, proxy2;
     *
     */
    public static String getXForwardedForddress() {
        return ((HttpServletRequest) getExternalContext().getRequest()).getHeader("x-forwarded-for");
    }

    public static String getSessionId() {
        return ((HttpServletRequest) getExternalContext().getRequest()).getSession().getId();
    }

    public static void removeAttributeInSession(String attribute) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(attribute);
    }

    public static Object getAttributeInSession(String attribute) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(attribute);
    }

//    public static Object setAttributeInSession(String attribute, Object value) {
//        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(attribute, value);
//    }
    public static Object getAttributeFromRequest(String attributeKey) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(attributeKey);
    }

    public static long getCreationTimeSession() {
        return ((HttpServletRequest) getExternalContext().getRequest()).getSession().getCreationTime();
    }

    public static void redirect(String url)
            throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalCtx = facesContext.getExternalContext();
        externalCtx.redirect(url);
    }

    public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory("javax.faces.lifecycle.LifecycleFactory");
            javax.faces.lifecycle.Lifecycle lifecycle = lifecycleFactory.getLifecycle("DEFAULT");
            FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory("javax.faces.context.FacesContextFactory");
            facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);
            javax.faces.component.UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
            facesContext.setViewRoot(view);
            FacesContextWrapper.setCurrentInstance(facesContext);
        }
        return facesContext;
    }

    /**
     * Helper method to get the origin IP of a chain of IP addresses.
     *
     * Example: client1, proxy1, proxy2
     *
     * The origin IP will be: client1
     *
     * @return client1
     */
    public static String getXForwaredForOrigin(String remoteAddr) {
        int idx = remoteAddr.indexOf(',');
        if (idx > -1) {
            remoteAddr = remoteAddr.substring(0, idx);
        }
        return remoteAddr;
    }
}
