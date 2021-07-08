package com.stech.csw.crawler.news.api.transformer;

import com.stech.csw.crawler.news.api.model.News;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsTransformar {

    private static final Logger logger = LoggerFactory.getLogger(NewsTransformar.class);

    @Value("#{'${cramler.news.api.hit.words}'.split(',')}")
    private List<String> hitWords;

    @Value("${cramler.news.api.logs.enabled}")
    private String logsEnabled;

    public News transformToNews(Page page){
        if (null != page){
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document document = Jsoup.parse(htmlParseData.getHtml());
            News news = new News();
            news.setTitle(htmlParseData.getTitle());
            news.setLink(page.getWebURL().getURL());
            news.setContent(document.body().text());
            news.setRank(getRank(document));
            if("true".equalsIgnoreCase(logsEnabled)) {
                logger.info("Title {}", news.getTitle());
                logger.info("Link {}", news.getLink());
                logger.info("Rank {}", news.getRank());
            }
            return news;
        }
        return null;
    }

    /**
     * Ranking Logic by Hit words
     * @param document
     * @return Rank 0-LOW, 1-MEDIUM, 2-HIGH
     */
    private Integer getRank(Document document){
        Integer rank = 0;
        String hitString =null;
        if (null != hitWords){
            hitString = hitWords.stream().filter(hit -> document.title().toUpperCase().contains(hit)).findAny().orElse(null);
            if (null != hitString){
                rank = 2;
                return rank;
            }
            hitString = hitWords.stream().filter(hit -> document.body().text().toUpperCase().contains(hit)).findAny().orElse(null);
            if (null != hitString){
                rank = 1;
                return rank;
            }
        }
        return rank;
    }

}
