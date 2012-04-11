/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.metallium.core.log.interceptor;

import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.AuthenticationException;
import co.com.metallium.core.log.service.InterceptorService;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * 20101214
 * @author Ruben
 */
public class InterceptorUserAuthentication {


    @EJB
    InterceptorService service;

    @AroundInvoke
    public User interceptor(InvocationContext ic) throws Exception {
        User answer = null;
        User userToAuthenticate = null;
        try {

            userToAuthenticate = (User) ic.getParameters()[0];
            answer = (User)ic.proceed();
            service.logUserAuthentication(userToAuthenticate,1);
        } catch (AuthenticationException e) {
            service.logUserAuthentication(userToAuthenticate, 0, e.getMessage());
            throw e;
        } catch (Exception e) {
            service.logUserAuthentication(userToAuthenticate, 2, e.getMessage());
            throw e;
        }
        return answer;
    }


}
