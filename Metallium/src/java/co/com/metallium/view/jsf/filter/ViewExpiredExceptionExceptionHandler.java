/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.filter;

import co.com.metallium.view.util.JsfUtil;
import com.metallium.utils.framework.utilities.LogHelper;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Taken from
 * http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2
 *
 * 20101217
 * @author Ruben
 */
public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {

            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            if (t instanceof ClassCastException) {

                /**
                 * Look im getting a fucking class cast exception something like this:
                 *
                 * java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to java.util.Map
                 *      at com.sun.faces.application.view.StateManagementStrategyImpl.restoreView(StateManagementStrategyImpl.java:212)
                 *      at com.sun.faces.application.StateManagerImpl.restoreView(StateManagerImpl.java:177)
                 *
                 *  It is driving me crazy and I see no way of fixing it. So just ignore the dame exception and thats it,
                 *  because Its too annoying so see a stack trace every time, for an exception thats just always there.
                 */
                
                LogHelper.makeLog("ViewExpiredExceptionExceptionHandler --> Holy Shit ClassCastException!!! ");
                i.remove();
            }


            if (t instanceof ViewExpiredException) {

//                LogHelper.makeLog("Holy Shit ViewExpiredException!!!");
//                LogHelper.makeLog(JsfUtil.getRequestUri() + "---" + JsfUtil.getFacesRequestUri());
                ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {

                    /**
                     * So, what Im gonna do is get the page the user requested,
                     * and redirect him to that page anyway.
                     *
                     */
                    
                    String url = JsfUtil.getFacesRequestUri();
                    if (url.contains(".")) {
                        url = url.substring(0, url.lastIndexOf("."));
                    }

                    nav.handleNavigation(fc, null, url);
                    fc.renderResponse();

                } finally {
                    i.remove();
                }
            }
        }

        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();

    }
}
