
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

import co.com.metallium.core.constants.state.UserFriendState;
import co.com.metallium.core.constants.state.UserState;
import co.com.metallium.core.entity.News;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.exception.SearchException;
import co.com.metallium.core.service.dto.search.CommentsSearchDTO;
import co.com.metallium.core.service.dto.search.EventSearchDTO;
import co.com.metallium.core.service.dto.search.ForumSearchDTO;
import co.com.metallium.core.service.dto.search.GeneralSearchDTO;
import co.com.metallium.core.service.dto.search.NewsSearchDTO;
import co.com.metallium.core.service.dto.search.UserFriendsSearchDTO;
import co.com.metallium.core.service.dto.search.UserWallSearchDTO;
import com.metallium.utils.dto.SqlAddOnDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.DateHelper;
import com.metallium.utils.search.SearchBaseDTO;
import com.metallium.utils.utils.UtilHelper;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 *
 * @author Ruben
 */
public class SearchHelper {

    public SearchHelper() {
    }

    public static boolean isSearchCriteriaEmpty(SearchBaseDTO obj) throws SearchException {

        if (obj instanceof GeneralSearchDTO) {
            return isUserSearchCriteriaEmpty((GeneralSearchDTO) obj);
        } else if (obj instanceof EventSearchDTO) {
            return isEventSearchCriteriaEmpty((EventSearchDTO) obj);
        } else if (obj instanceof NewsSearchDTO) {
            return isNewsSearchCriteriaEmpty((NewsSearchDTO) obj);
        } else if (obj instanceof UserFriendsSearchDTO) {
            return isUserFriendsCriteriaEmpty((UserFriendsSearchDTO) obj);
        } else if (obj instanceof CommentsSearchDTO) {
            return isCommentsCriteriaEmpty((CommentsSearchDTO) obj);
        } else if (obj instanceof ForumSearchDTO) {
            return isForumCriteriaEmpty((ForumSearchDTO) obj);
        } else if (obj instanceof UserWallSearchDTO) {
            return isUserWallEntryCriteriaEmpty((UserWallSearchDTO) obj);
        }

        throw new SearchException("Object: " + obj + ", is not wired in method 'isSearchCriteriaEmpty'");
    }

    public static SqlAddOnDTO buildSearchFilters(SearchBaseDTO obj, Collection<String> filters, HashMap<String, Object> parameters) throws SearchException {

        if (obj instanceof GeneralSearchDTO) {
            return buildUserSearchFilters((GeneralSearchDTO) obj, filters, parameters);
        } else if (obj instanceof EventSearchDTO) {
            return buildEventSearchFilters((EventSearchDTO) obj, filters, parameters);
        } else if (obj instanceof NewsSearchDTO) {
            return buildNewsSearchFilters((NewsSearchDTO) obj, filters, parameters);
        } else if (obj instanceof UserFriendsSearchDTO) {
            return buildUserFriendsFilters((UserFriendsSearchDTO) obj, filters, parameters);
        } else if (obj instanceof CommentsSearchDTO) {
            return buildUserCommentsFilters((CommentsSearchDTO) obj, filters, parameters);
        } else if (obj instanceof ForumSearchDTO) {
            return buildForumSearchFilters((ForumSearchDTO) obj, filters, parameters);
        } else if (obj instanceof UserWallSearchDTO) {
            return buildUserWallEntrySearchFilters((UserWallSearchDTO) obj, filters, parameters);
        }

        throw new SearchException("Object: " + obj + ", is not wired in method 'buildSearchFilters'");
    }

    /**
     *
     * @param searchDTO
     * @return false = it has a search filter
     *         true = it doesn't have any filters, its all NULL or empty
     *
     */
    private static boolean isUserSearchCriteriaEmpty(GeneralSearchDTO searchDTO) {
        return UtilHelper.isStringEmpty(searchDTO.getSex()) && UtilHelper.isStringEmpty(searchDTO.getCity())
                && UtilHelper.isStringEmpty(searchDTO.getName())
                && UtilHelper.isStringEmpty(searchDTO.getEmail()) && UtilHelper.isStringEmpty(searchDTO.getCountry())
                && DataHelper.isNull(searchDTO.getFriendsOfUserId()) && DataHelper.isNull(searchDTO.getState());
    }

    private static boolean isEventSearchCriteriaEmpty(EventSearchDTO searchDTO) {

        return UtilHelper.isStringEmpty(searchDTO.getState()) && DataHelper.isNull(searchDTO.getNetwork())
                && DataHelper.isNull(searchDTO.getDate1()) && DataHelper.isNull(searchDTO.getDate2())
                && DataHelper.isNull(searchDTO.getUserIdCreated()) && DataHelper.isNull(searchDTO.getEventId())
                && UtilHelper.isStringEmpty(searchDTO.getSearch());

    }

    private static boolean isUserWallEntryCriteriaEmpty(UserWallSearchDTO searchDTO) {
        return DataHelper.isNull(searchDTO.getUserId());
    }

    public static boolean isForumCriteriaEmpty(ForumSearchDTO searchDTO) {
        return UtilHelper.isStringEmpty(searchDTO.getState()) && DataHelper.isNull(searchDTO.getNetwork())
                && DataHelper.isNull(searchDTO.getDate1()) && DataHelper.isNull(searchDTO.getDate2())
                && DataHelper.isNull(searchDTO.getId()) && DataHelper.isNull(searchDTO.getId())
                && UtilHelper.isStringEmpty(searchDTO.getSearch())
                && DataHelper.isNull(searchDTO.getCategory());
    }

    private static boolean isNewsSearchCriteriaEmpty(NewsSearchDTO searchDTO) {
        return UtilHelper.isStringEmpty(searchDTO.getState()) && DataHelper.isNull(searchDTO.getNetwork())
                && DataHelper.isNull(searchDTO.getDate1()) && DataHelper.isNull(searchDTO.getDate2())
                && DataHelper.isNull(searchDTO.getUserIdPublisher()) && DataHelper.isNull(searchDTO.getNewsId());
    }

    private static boolean isUserFriendsCriteriaEmpty(UserFriendsSearchDTO searchDTO) {
        return UtilHelper.isStringEmpty(searchDTO.getUserNick()) && DataHelper.isNull(searchDTO.getUserId())
                && DataHelper.isNull(searchDTO.getState());
    }

    private static boolean isCommentsCriteriaEmpty(CommentsSearchDTO searchDTO) {
        return UtilHelper.isStringEmpty(searchDTO.getState()) && DataHelper.isNull(searchDTO.getCommentId())
                && DataHelper.isNull(searchDTO.getDate1()) && DataHelper.isNull(searchDTO.getDate2())
                && DataHelper.isNull(searchDTO.getUserId()) && DataHelper.isNull(searchDTO.getEntityId())
                && UtilHelper.isStringEmpty(searchDTO.getImageId()) && UtilHelper.isStringEmpty(searchDTO.getType());
    }

    private static SqlAddOnDTO buildUserSearchFilters(GeneralSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {
        SqlAddOnDTO sqlAddOn = null;

        if (!UtilHelper.isStringEmpty(searchDTO.getSex())) {
            filters.add("u.sex = :sex");
            parameters.put("sex", searchDTO.getSex());
        }
        if (!UtilHelper.isStringEmpty(searchDTO.getName())) {
            filters.add("u.name LIKE :name");
            parameters.put("name", putLikeParameter(searchDTO.getName()));
        }
        if (!UtilHelper.isStringEmpty(searchDTO.getEmail())) {
            filters.add("u.email = :email");
            parameters.put("email", searchDTO.getEmail());
        }
        if (!UtilHelper.isStringEmpty(searchDTO.getCountry())) {
            filters.add("u.country.iso = :country");
            parameters.put("country", searchDTO.getCountry());
        }
        if (!UtilHelper.isStringEmpty(searchDTO.getCity())) {
            filters.add("u.city LIKE :city");
            parameters.put("city", putLikeParameter(searchDTO.getCity()));
        }

        if (!DataHelper.isNull(searchDTO.getFriendsOfUserId())) {

            parameters.put("id", searchDTO.getFriendsOfUserId());
            String extraParameters = "( uf.state = ".concat(UserFriendState.FRIENDSHIP_APPROVED.toString()).concat(" AND  uf.user1.id = :id AND uf.user2.id = u.id ) ");

            //If there are filters then that means that there are going to be more paramters besides these 'extraParameters', so I
            //want to make sure that the AND clause is used after the 'extraParameters'
            if (filters.size() > 0) {
                extraParameters = extraParameters.concat(" AND ");
            }

            sqlAddOn = new SqlAddOnDTO(User.entityAddOnQueryFragment1, extraParameters);
        }

        /** todo, fix this mess **/
        if (searchDTO.getState() != null && !searchDTO.getState().isEmpty()) {
            parameters.put("state", searchDTO.getState());

            String extraParameters = "( u.state IN :state) ";

            //If there are filters then that means that there are going to be more paramters besides these 'extraParameters', so I
            //want to make sure that the AND clause is used after the 'extraParameters'
            if (filters.size() > 0) {
                extraParameters = extraParameters.concat(" AND ");
            }
            sqlAddOn = new SqlAddOnDTO(null, extraParameters);
        }



        
        return sqlAddOn;
    }

    private static SqlAddOnDTO buildEventSearchFilters(EventSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {

        SqlAddOnDTO sqlAddOn = null;

        if (!UtilHelper.isStringEmpty(searchDTO.getState())) {
            filters.add("e.state = :state");
            parameters.put("state", searchDTO.getState());
        }


        if (!DataHelper.isNull(searchDTO.getDate1())) {
            filters.add("e.eventDate >= :eventDate1");
            parameters.put("eventDate1", DateHelper.setHoraEnCero(searchDTO.getDate1()));
        }


        if (!DataHelper.isNull(searchDTO.getDate2())) {
            filters.add("e.eventDate <= :eventDate2");
            parameters.put("eventDate2", DateHelper.setHoraEn235959(searchDTO.getDate2()));
        }

        if (!DataHelper.isNull(searchDTO.getUserIdCreated())) {
            filters.add("e.userCreated = :userCreated");
            parameters.put("userCreated", searchDTO.getUserIdCreated());
        }

        if (!DataHelper.isNull(searchDTO.getEventId())) {
            filters.add("e.id = :id");
            parameters.put("id", searchDTO.getEventId());
        }

        if (!DataHelper.isNull(searchDTO.getNetwork())) {
            filters.add("e.network = :network");
            parameters.put("network", searchDTO.getNetwork());
        }

        return sqlAddOn;
    }

    private static SqlAddOnDTO buildUserWallEntrySearchFilters(UserWallSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {
        SqlAddOnDTO sqlAddOn = null;

        if (!DataHelper.isNull(searchDTO.getUserId())) {
            filters.add("u.userId = :userId");
            parameters.put("userId", searchDTO.getUserId());
        }
        return sqlAddOn;
    }

    private static SqlAddOnDTO buildForumSearchFilters(ForumSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {

        SqlAddOnDTO sqlAddOn = null;

        if (!UtilHelper.isStringEmpty(searchDTO.getState())) {
            filters.add("f.state = :state");
            parameters.put("state", searchDTO.getState());
        }


        if (!DataHelper.isNull(searchDTO.getDate1())) {
            filters.add("f.date >= :date");
            parameters.put("date", DateHelper.setHoraEnCero(searchDTO.getDate1()));
        }


        if (!DataHelper.isNull(searchDTO.getDate2())) {
            filters.add("f.date <= :date");
            parameters.put("date", DateHelper.setHoraEn235959(searchDTO.getDate2()));
        }

        if (!DataHelper.isNull(searchDTO.getUserId())) {
            filters.add("f.user.id = :id");
            parameters.put("id", searchDTO.getUserId());
        }

        if (!DataHelper.isNull(searchDTO.getId())) {
            filters.add("f.id = :id");
            parameters.put("id", searchDTO.getId());
        }

        if (!DataHelper.isNull(searchDTO.getNetwork())) {
            filters.add("f.network = :network");
            parameters.put("network", searchDTO.getNetwork());
        }

        if (!DataHelper.isNull(searchDTO.getCategory())) {
            filters.add("f.category.id = :categoryId");
            parameters.put("categoryId", searchDTO.getCategory());
        }
        
        return sqlAddOn;
    }

    private static SqlAddOnDTO buildUserCommentsFilters(CommentsSearchDTO commentsSearchDTO, Collection<String> filters, HashMap<String, Object> parameters) {
        SqlAddOnDTO sqlAddOn = null;

        if (!DataHelper.isNull(commentsSearchDTO.getEntityId())) {
            filters.add("n.entityId = :entityId");
            parameters.put("entityId", commentsSearchDTO.getEntityId());
        }

        if (!UtilHelper.isStringEmpty(commentsSearchDTO.getImageId())) {
            filters.add("n.imageId = :imageId");
            parameters.put("imageId", commentsSearchDTO.getImageId());
        }

        if (!UtilHelper.isStringEmpty(commentsSearchDTO.getState())) {
            filters.add("n.state = :state");
            parameters.put("state", commentsSearchDTO.getState());
        }

        if (!UtilHelper.isStringEmpty(commentsSearchDTO.getType())) {
            filters.add("n.type = :type");
            parameters.put("type", commentsSearchDTO.getType());
        }

        return sqlAddOn;
    }

    private static SqlAddOnDTO buildNewsSearchFilters(NewsSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {

        SqlAddOnDTO sqlAddOn = null;

        if (!UtilHelper.isStringEmpty(searchDTO.getState())) {
            filters.add("u.state = :state");
            parameters.put("state", searchDTO.getState());
        }

        if (!DataHelper.isNull(searchDTO.getNetwork())) {
            filters.add("nn.newsNetworkPK.idNetwork = :idNetwork");
            parameters.put("idNetwork", searchDTO.getNetwork());
//            sqlAddOn = News.entityAddOnQueryFragment1;
            sqlAddOn = new SqlAddOnDTO(News.entityAddOnQueryFragment1, null);

            filters.add("nn.newsNetworkPK.idNews = u.id");

        }

        return sqlAddOn;
    }

    private static SqlAddOnDTO buildUserFriendsFilters(UserFriendsSearchDTO searchDTO, Collection<String> filters, HashMap<String, Object> parameters) {

        SqlAddOnDTO sqlAddOn = null;

        if (!DataHelper.isNull(searchDTO.getState())) {
            filters.add(" uf.state = :state");
            parameters.put("state", searchDTO.getState());
        }

        if (UtilHelper.isStringNotEmpty(searchDTO.getUserNick())) {
            filters.add(" uf.user1.nick = :nick");
            parameters.put("nick", searchDTO.getUserId());
            sqlAddOn = new SqlAddOnDTO(User.entityAddOnQueryFragment1, null);
            filters.add("uf.user2.nick = u.nick");
        }

        if (!DataHelper.isNull(searchDTO.getUserId())) {
            filters.add(" uf.user1.id = :id");
            parameters.put("id", searchDTO.getUserId());
//            sqlAddOn = User.entityAddOnQueryFragment1;
            sqlAddOn = new SqlAddOnDTO(User.entityAddOnQueryFragment1, null);
            filters.add("uf.user2.id = u.id");
        }

        return sqlAddOn;

    }

    /**
     * General Purpose method
     *
     * @param parameter
     * @return
     */
    private static String putLikeParameter(String parameter) {
        return parameter.concat("%");
    }

    /**
     * General Purpose method
     *
     * @param parameter
     * @return
     */
    private static String putDoubleLikeParameter(String parameter) {
        return "%".concat(parameter).concat("%");
    }

    private static void buildXxxFiltersExample(CommentsSearchDTO criterios, Collection<String> filtros, HashMap<String, Object> parametros) {

        if (criterios.getDate1() != null) {
            filtros.add("l.date >= :date1");
            parametros.put("date1", criterios.getDate1());
        }

        if (criterios.getDate2() != null) {
            filtros.add("l.date <= :date2");
            //Hay que sumarle un dia a la fecha 2, con el fin de que,
            //en el filtro de fechas, me traiga registros de ese dia, sino me trae registros
            //del dia anterior.
            GregorianCalendar fecha2MasUnDia = new GregorianCalendar();
            fecha2MasUnDia.setTime(criterios.getDate2());
            fecha2MasUnDia.add(Calendar.DATE, 1);
            parametros.put("date2", fecha2MasUnDia.getTime());
        }

        if (criterios.getState() != null && !criterios.getState().trim().equals("")) {
            filtros.add("l.state = :state");
            parametros.put("state", criterios.getState());
        }

        if (criterios.getCommentId() != null) {
            filtros.add("l.id = :id");
            parametros.put("id", criterios.getCommentId());
        }

        if (criterios.getEntityId() != null) {
            filtros.add("l.news.id = :newsId");
            parametros.put("newsId", criterios.getEntityId());
        }

        if (criterios.getUserId() != null) {
            filtros.add("l.user.id = :userId");
            parametros.put("userId", criterios.getUserId());
        }

    }
}
