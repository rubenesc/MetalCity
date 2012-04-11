/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.log.interceptor;

import co.com.metallium.core.log.service.InterceptorService;
import com.metallium.utils.framework.utilities.LogHelper;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * 20101123
 * @author Ruben
 */
public class InterceptorX {
    
    @EJB
    InterceptorService service;

    @AroundInvoke
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void interceptor(InvocationContext ic) throws Exception {
        try {

            Object obj = (Object) ic.getParameters()[0];
            LogHelper.makeLog("Obj: " + obj);

            ic.proceed();

            service.makeGoodLog();

        } catch (Exception e) {
            service.makeBadLog();
            throw e;
        }
    }

}
