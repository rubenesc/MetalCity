/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import com.metallium.utils.dto.SqlAddOnDTO;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.search.JpqlUtil;
import com.metallium.utils.search.SearchBaseDTO;
import com.metallium.utils.utils.TimeWatch;
import com.metallium.utils.utils.UtilHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * 20110107
 * @author Ruben
 */
@Stateless
public class SearchService {

    @PersistenceContext(unitName = "MetalliumPU")
    private EntityManager em;

    public SearchService() {
    }

    public <T> List<T> findQueryByRange(SearchBaseDTO searchDTO) {
        TimeWatch watch = TimeWatch.start();

        List<T> answer = null;

        try {
            Query q = buildQuery(searchDTO, false);
            answer = q.getResultList();
        } catch (Exception e) {
            answer = new ArrayList<T>();
            LogHelper.makeLog(e.getMessage(), e);
        }
        LogHelper.makeLog("SearchService.findQueryByRange time: " + watch.timeDes());
        return answer;
    }

    public int countQuery(SearchBaseDTO searchDTO) {
        TimeWatch watch = TimeWatch.start();
        int answer = 0;
        try {
            Query q = buildQuery(searchDTO, true);
            answer = ((Number) q.getSingleResult()).intValue();

        } catch (Exception e) {
            LogHelper.makeLog(e.getMessage(), e);
        }

        LogHelper.makeLog("SearchService.countQuery time: " + watch.timeDes());
        return answer;
    }

    /**
     *
     * @param searchDTO = contains the parameters to make the search.
     * @param isItToCount = Is the query to Count how many Items are in the search, or to Retrieve the Items of the search.
     * @return Query = Ready to execute the search.
     */
    private Query buildQuery(SearchBaseDTO searchDTO, boolean isItToCount) {

        Query jpaQuery = null;

        String searchQuery = null;
        String searchOrderByQuery = null;

        if (isItToCount) {
            searchQuery = UtilHelper.reflectionGetStringPropertyFromClass(searchDTO.getTheClass(), "entityCountQueryFragment"); //Example: "Select COUNT(DISTINCT u.id) FROM User u "
            searchOrderByQuery = ""; //If you are going to just Count, then you dont need an ORDER BY in your SQL.
        } else {
            searchQuery = UtilHelper.reflectionGetStringPropertyFromClass(searchDTO.getTheClass(), "entitySearchQueryFragment"); //Example: "SELECT u FROM User u"

            if (!StringUtils.isEmpty(searchDTO.getOrderBy())) {
                //If they specify an order by in the OrderBy parameter then I take that one
                searchOrderByQuery = searchDTO.getOrderBy();
            } else {
                //I take the default order by of the Entity Im going to find
                searchOrderByQuery = UtilHelper.reflectionGetStringPropertyFromClass(searchDTO.getTheClass(), "entityOrderByQueryFragment"); //Example: " order by u.nick ASC"
            }
        }

        HashMap<String, Object> searchParameters = new HashMap<String, Object>();
        Collection<String> filters = new ArrayList<String>();

        try {
            //I check to see if there are any defined search conditions in the SearchDTO.
            if (SearchHelper.isSearchCriteriaEmpty(searchDTO)) {
                //This means that the Query will not have a WHERE element
                searchQuery += searchOrderByQuery;

            } else {
                //There are search conditions defined in the SearchDTO, so, the query will have a WHERE element.
                SqlAddOnDTO searchQueryAddOn = SearchHelper.buildSearchFilters(searchDTO, filters, searchParameters);

                //This search addon is to add more dynamic tables to the ones defined in the variable
                //'searchQuery' I did this because depending on some conditions I had to do a join between two tables. This was the case for NewsNetwork.
                if (searchQueryAddOn != null && !UtilHelper.isStringEmpty(searchQueryAddOn.getTables())) {
                    searchQuery = searchQuery.concat(searchQueryAddOn.getTables());
                }

                if (searchQueryAddOn != null && !UtilHelper.isStringEmpty(searchQueryAddOn.getParameters())) {
                    //Finally I build the Sql Sentence Im going to execute withe the given conditions.
                    searchQuery += JpqlUtil.buildJpqlSentence(filters, searchDTO.getSqlCondition(), searchOrderByQuery, searchQueryAddOn.getParameters());
                } else {
                    //Finally I build the Sql Sentence Im going to execute withe the given conditions.
                    searchQuery += JpqlUtil.buildJpqlSentence(filters, searchDTO.getSqlCondition(), searchOrderByQuery, null);
                }

            }

            // Search query
            jpaQuery = em.createQuery(searchQuery);

            // I add the parameters to the query
            JpqlUtil.fillJpqlSentence(jpaQuery, searchParameters);

            /*
            Query q = null;
            q = em.createQuery(searchQuery);
            q.setParameter("nick", "%" + criteriaDTO.getQuery() + "%");
            //            q.setParameter("esc", '\\');
             */


            if (!isItToCount) {
                //If it is just a count query then I dont need this.
                int[] range = searchDTO.getRange();
                jpaQuery.setMaxResults(range[1] - range[0]);
                jpaQuery.setFirstResult(range[0]);
            }


        } catch (Exception ex) {
            //May god help us all!!!
            LogHelper.makeLog(ex.getMessage(), ex);
        }

        return jpaQuery;
    }
}
