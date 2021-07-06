package com.stech.csw.crawler.news.api.service;

import com.google.common.collect.ImmutableList;
import com.stech.csw.crawler.news.api.model.News;
import com.stech.csw.crawler.news.api.repository.NewsRepository;
import com.stech.csw.crawler.news.api.transformer.NewsTransformar;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class BasicCrawlerService extends WebCrawler {

    private final NewsTransformar newsTransformar;

    private final NewsRepository newsRepository;

    private final List<String> myCrawlDomains;

    private static final Logger logger = LoggerFactory.getLogger(BasicCrawlerService.class);

    private static final Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
                    "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public BasicCrawlerService(List<String> myCrawlDomains, NewsRepository newsRepository, NewsTransformar newsTransformar) {
        this.myCrawlDomains = ImmutableList.copyOf(myCrawlDomains);
        this.newsRepository = newsRepository;
        this.newsTransformar = newsTransformar;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        //Only Visit Given Domain
        for (String crawlDomain : myCrawlDomains) {
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        int parentDocid = page.getWebURL().getParentDocid();

        logger.debug("Docid: {}", docid);
        logger.info("Start Page/URL: {}", url);
        logger.debug("Docid of parent page: {}", parentDocid);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            //Transform & Persist
            News news = newsTransformar.transformToNews(page);
            if (null != news && !isExits(news)) {
                newsRepository.save(news);
            }
        }
        logger.debug("Page completed");
    }

    /**
     *
     * @param news
     * @return
     */
    private boolean isExits(News news){
        News exits = newsRepository.findByTitleAndLink(news.getTitle(), news.getLink());
        if (exits != null){
            logger.warn("News Exits! with Title {} Link {}", news.getTitle(), news.getLink());
        }
        return exits != null ? true : false;
    }

}
