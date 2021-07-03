package com.stech.csw.crawler.news.api.controller;

import com.stech.csw.crawler.news.api.service.CrawlerTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CrawlerController {

    @Autowired
    CrawlerTriggerService crawlerTriggerService;

    @GetMapping("/crawler")
    public void startCrawler() throws Exception {
        crawlerTriggerService.startCrawler();
    }
}
