/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.log.service;

import co.com.metallium.core.entity.LogUserAuthentication;
import co.com.metallium.core.entity.LogUserDelete;
import co.com.metallium.core.entity.User;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.UtilHelper;
import java.util.Date;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 20101123
 * @author Ruben
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class InterceptorService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;

    public InterceptorService() {
    }

    public void makeBadLog() {
        LogHelper.makeLog("Estmos en hacer BAD log");
    }

    public void makeGoodLog() {
        LogHelper.makeLog("Estmos en hacer GOOD log");
    }

    @Asynchronous
    public void logUserAuthentication(User user, Integer result) {

        try {
            LogUserAuthentication log = populateLogUserAuthentication(user, result);
            em.persist(log);
            em.flush();
        } catch (Exception ex) {
            LogHelper.makeLog("Error: " + ex.getMessage(), ex);
        }
    }

    @Asynchronous
    public void logUserAuthentication(User user, int result, String error) {

        try {
            LogUserAuthentication log = populateLogUserAuthentication(user, result);
            log.setError(this.verifyStringLenth(error));
            em.persist(log);
            em.flush();
        } catch (Exception ex) {
            LogHelper.makeLog("Error: " + ex.getMessage(), ex);
        }
    }

    private LogUserAuthentication populateLogUserAuthentication(User user, Integer result) {
        LogUserAuthentication log = new LogUserAuthentication();
        log.setEmail(user.getEmail());
        log.setSessionId(user.getSessionId());
        log.setIp(user.getIpAddress());
        log.setDate(new Date());
        log.setResult(result);
        log.setLocation(user.getLocationId());
        return log;
    }



    @Asynchronous
    public void logUserDelete(User user, String whyDidYouLeave) {

        try {
            LogUserDelete log = new LogUserDelete();
            log.setUserId(user.getId());
            log.setEmail(user.getEmail());
            log.setSessionId(user.getSessionId());
            log.setIp(user.getIpAddress());
            log.setDate(new Date());
            log.setWhy(this.verifyStringLenth(whyDidYouLeave));
            em.persist(log);
            em.flush();
        } catch (Exception ex) {
            LogHelper.makeLog("Error: " + ex.getMessage(), ex);
        }
    }



    /**
     * This is to avoid that if an exception or error exceeds a certain length longer
     * than the one in the DB, then I can just trim it and thats it.
     * @param str
     * @return
     */
    private String verifyStringLenth(String str) {
        if (!UtilHelper.isStringEmpty(str) && str.length() > 500) {
            return str.substring(0, 499);
        } else {
            return str;
        }
    }
}
