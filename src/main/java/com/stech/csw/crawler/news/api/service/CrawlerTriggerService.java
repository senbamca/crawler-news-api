package com.stech.csw.crawler.news.api.service;

import com.google.common.collect.ImmutableList;
import com.stech.csw.crawler.news.api.repository.NewsRepository;
import com.stech.csw.crawler.news.api.transformer.NewsTransformar;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlerTriggerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerTriggerService.class);

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    NewsTransformar newsTransformar;

    @Value("#{'${cramler.news.api.crawler1.domains}'.split(',')}")
    private List<String> crawler1Domains;

    @Value("#{'${cramler.news.api.crawler2.domains}'.split(',')}")
    private List<String> crawler2Domains;

    @Value("#{'${cramler.news.api.crawler1.seedurls}'.split(',')}")
    private List<String> crawler1SeedUrls;

    @Value("#{'${cramler.news.api.crawler2.seedurls}'.split(',')}")
    private List<String> crawler2SeedUrls;

    @Value("${cramler.news.api.max.fetch.pages}")
    private Integer maxPages;

    public void startCrawler() throws Exception {
        // The folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        String crawlStorageFolder = "/tmp/crawler4j/";

        CrawlConfig config1 = new CrawlConfig();
        CrawlConfig config2 = new CrawlConfig();

        // The two crawlers should have different storage folders for their intermediate data.
        config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
        config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

        //Time delay to fetch
        config1.setPolitenessDelay(1000);
        config2.setPolitenessDelay(1000);

        //Fetch limit
        config1.setMaxPagesToFetch(maxPages);
        config2.setMaxPagesToFetch(maxPages);

        // We will use different PageFetchers for the two crawlers.
        PageFetcher pageFetcher1 = new PageFetcher(config1);
        PageFetcher pageFetcher2 = new PageFetcher(config2);

        // We will use the same RobotstxtServer for both of the crawlers.
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

        CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
        CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);

        //List<String> crawler1Domains = ImmutableList.of("https://www.ics.uci.edu/", "https://www.cnn.com/");
        //List<String> crawler2Domains = ImmutableList.of("https://en.wikipedia.org/");

        /*controller1.addSeed("https://www.ics.uci.edu/");
        controller1.addSeed("https://www.cnn.com/");
        controller1.addSeed("https://www.ics.uci.edu/~lopes/");
        controller1.addSeed("https://www.cnn.com/POLITICS/");

        controller2.addSeed("https://en.wikipedia.org/wiki/Main_Page");
        controller2.addSeed("https://en.wikipedia.org/wiki/Obama");
        controller2.addSeed("https://en.wikipedia.org/wiki/Bing");*/

        for (String url :crawler1SeedUrls){
            controller1.addSeed(url);
        }
        for (String url :crawler2SeedUrls){
            controller2.addSeed(url);
        }



        //Initiate
        CrawlController.WebCrawlerFactory<BasicCrawlerService> factory1 = () -> new BasicCrawlerService(crawler1Domains, newsRepository, newsTransformar);
        CrawlController.WebCrawlerFactory<BasicCrawlerService> factory2 = () -> new BasicCrawlerService(crawler2Domains, newsRepository, newsTransformar);

        // The first crawler will have 5 concurrent threads and the second crawler will have 7 threads.
        controller1.startNonBlocking(factory1, 5);
        controller2.startNonBlocking(factory2, 7);

        controller1.waitUntilFinish();
        logger.info("Crawler 1 is finished.");

        controller2.waitUntilFinish();
        logger.info("Crawler 2 is finished.");
    }
}


