/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.ForumCategoryStateEnum;
import co.com.metallium.core.entity.Configuration;
import co.com.metallium.core.entity.Country;
import co.com.metallium.core.entity.ForumCategory;
import co.com.metallium.core.entity.Network;
import co.com.metallium.core.entity.Profile;
import co.com.metallium.core.entity.ReservedNames;
import co.com.metallium.core.exception.CommonException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ruben
 */
@Stateless
public class CommonService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;

    public CommonService() {
    }

    //========================COUNTRY==========================================//
    public Country findCountryByIso(String iso) throws CommonException {
        Country entity = null;

        try {
            entity = (Country) em.createNamedQuery("Country.findByIso").setParameter("iso", iso).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The country with iso: " + iso + ", could not be found in the system.";
                throw new CommonException(msg, ex);
            } else {
                throw new CommonException(ex.getMessage(), ex);
            }
        }
        return entity;
    }

    public List<Country> findCountryList() throws CommonException {
        List<Country> answer = null;
        try {
            answer = em.createNamedQuery("Country.findAll").getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }

        return answer;
    }

    //========================PROFILE==========================================//
    public Profile findProfileById(Integer id) throws CommonException {
        Profile entity = null;

        try {
            entity = (Profile) em.createNamedQuery("Profile.findById").setParameter("id", id).getSingleResult();
        } catch (Exception ex) {

            if (ex instanceof NoResultException) {
                String msg = "The Profile with id: " + id + ", could not be found in the system.";
                throw new CommonException(msg, ex);
            } else {
                throw new CommonException(ex.getMessage(), ex);
            }
        }
        return entity;
    }

    public List<Profile> findProfileList() throws CommonException {
        List<Profile> answer = null;
        try {
            answer = em.createNamedQuery("Profile.findAll").getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }

        return answer;
    }

    //========================Configuration==========================================//
    public List<Configuration> findConfigurationList() throws CommonException {
        List<Configuration> answer = null;
        try {
            answer = em.createNamedQuery("Configuration.findAll").getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }

        return answer;
    }
    //========================Network==========================================//

    public List<Network> findNetworkList() throws CommonException {
        List<Network> answer = null;
        try {
            answer = em.createNamedQuery("Network.findAll").getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }

        return answer;
    }
    //========================Forum==========================================//

    public List<ForumCategory> findForumCategoryList() throws CommonException {
        List<ForumCategory> answer = null;
        try {
            answer = em.createNamedQuery("ForumCategory.findAll").getResultList();
//            answer = em.createNamedQuery("ForumCategory.findByState").setParameter("state", ForumCategoryStateEnum.ACTIVE).getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }

        return answer;
    }

    //========================Reserved Names===================================//
    public List<ReservedNames> findReservedNamesList() throws CommonException {
        List<ReservedNames> answer = null;
        try {
            answer = em.createNamedQuery("ReservedNames.findAll").getResultList();
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage(), ex);
        }
        return answer;
    }
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
    //========================PROFILE==========================================//
}
