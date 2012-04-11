/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import com.metallium.utils.framework.utilities.LogHelper;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.jpa.JpaEntityManager;

/**
 * 20111113
 * @author Ruben
 */
@Stateless
public class JpaCacheService {


    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;

    public void evictAll() {
        LogHelper.makeLog("JpaCacheService.evictAll() !!! ");
        ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().invalidateAll();
        ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().clearQueryCache();
        //((JpaEntityManager) em.getDelegate()).getServerSession()
    }
}
