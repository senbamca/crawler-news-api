package com.stech.csw.crawler.news.api.transformer;

import com.stech.csw.crawler.news.api.model.News;
import com.stech.csw.crawler.news.api.repository.NewsRepository;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NewsTransformar {

    public News transformToNews(Page page){
        if (null != page){
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document document = Jsoup.parse(htmlParseData.getHtml());
            News news = new News();
            news.setTitle(htmlParseData.getTitle());
            news.setLink(page.getWebURL().getURL());
            news.setContent(document.body().text());
            news.setSource("WEB");
            return news;
        }
        return null;
    }

}
