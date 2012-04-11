/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils.htmlscriptvalidator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import com.metallium.utils.dto.PairDTO;
import com.metallium.utils.framework.utilities.DataHelper;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.youtube.YouTubeClient;
import com.metallium.utils.youtube.YouTubeVideoDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 *
 * @author Ruben
 */
public class HtmlSanitizer {

    public static String deleteAllHtmlTags(String input) {
        return Jsoup.clean(input, Whitelist.simpleText());
    }

    public static String comment(String input) {
        return Jsoup.clean(input, Whitelist.basicWithImages());
    }

    public static String wallPost(String input) {
        return Jsoup.clean(input, Whitelist.relaxed());
    }

    public static String admin(String input) {
        return Jsoup.clean(input, Whitelist.relaxed());
    }

    /**
     *  converts  --> <a href='http://example.com/' onclick='stealCookies()'>Link</a>
     *            --> <a href="http://example.com/" rel="nofollow">Link</a>
     * 
     * @param link
     * @return 
     */
    public static String link(String link) {
        return Jsoup.clean(link, Whitelist.basic());
    }

    public static String processYouTubeVideoDescription(String videoId) throws Exception {
        String videoHtmlDisplayDesc = buildVideoHtmlDisplayDesc(findYouTubeVideoInfo(videoId));
        return videoHtmlDisplayDesc;
    }
    
    public static YouTubeVideoDTO findYouTubeVideoInfo(String videoId) throws Exception {
        YouTubeClient client = new YouTubeClient();
        return client.retrieveVideo(videoId);
    }
    

    public static String processYouTubeLinks(String input) {
        input = Jsoup.clean(input, Whitelist.basicWithImages());

        Document doc = Jsoup.parse(input);
        Elements links = doc.getElementsByTag("a");

        List<PairDTO> l = new ArrayList<PairDTO>();
        for (Element link : links) {
            String linkHref = link.attr("href");
            String outerHtml = link.outerHtml();

            String id = YouTubeClient.getYouTubeId(linkHref);
            if (id != null) {
                //It is a YouTube url
                l.add(new PairDTO(linkHref, outerHtml, id));
            }
        }

        int cont = 1;
        for (Iterator<PairDTO> it = l.iterator(); it.hasNext();) {
            PairDTO pairDTO = it.next();
            try {
                //            x = x.replace(pairDTO.getTwo(), "#_youtube_" + cont + "#");

                String videoHtmlDisplayDesc = processYouTubeVideoDescription(pairDTO.getThree());
                input = input.replace(pairDTO.getTwo(), videoHtmlDisplayDesc);

            } catch (Exception ex) {
                LogHelper.makeLog(ex);
            }

            cont++;
        }

        return input;
    }

    /**
     * This is an aux method that given some info builds a description of a 
     * resource, in this case a YouTube Video. 
     * 
     * Example: imgLink --> http://i.ytimg.com/vi/b3QL6QaKwZI/1.jpg
     *          title -->   Iron Maiden - Virus
     * 
     * and it returns an html script like this
     * 
     *      <div>
     *          IMAGE  "Iron Maiden - Virus"
     *      </div>
     *            
     * 
     * @param imgLink
     * @param title
     * @return 
     */
    private static String buildVideoHtmlDisplayDesc(YouTubeVideoDTO dto) {
        String x = null;
        
        x = "  "
                + "     <h3> "
                + "         <table cellspacing='5' cellpadding='5'><tr><td style='vertical-align: top' ><img  width='200' src='" + dto.getThumbnail1() + "' /></td> 		"
                + "            <span style='padding-left:8px'></span><td style='vertical-align: top'><b>" + dto.getTitle() + "</b><br/><br/><p>" + DataHelper.getMediaDescriptionString(dto.getDescription()) + "</p></td></tr></table></h3> "
                + " 	</h3> "
                + " ";
        return x;
    }
}
