package com.stech.csw.crawler.news.api.controller;


import com.stech.csw.crawler.news.api.repository.NewsRepository;
import com.stech.csw.crawler.news.api.exception.ResourceNotFoundException;
import com.stech.csw.crawler.news.api.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @Value("${cramler.news.api.trending.rank}")
    private Integer rank;

    @GetMapping("/news")
    public List<News> getAllNotes() {
        return newsRepository.findAll();
    }

    @PostMapping("/news")
    public News createNote(@Valid @RequestBody News news) {
        return newsRepository.save(news);
    }

    @GetMapping("/news/{id}")
    public News getNoteById(@PathVariable(value = "id") Long noteId) {
        return newsRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", noteId));
    }

    @GetMapping("/news/latest")
    public List<News> getLatestNews() {
        return newsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @GetMapping("/news/trending")
    public List<News> getTrendingNews() {
        return newsRepository.findByRank(rank);
    }

    @GetMapping("/news/search/{content}")
    public List<News> getNewsByKeyword(@PathVariable(value = "content") String content) {
        return newsRepository.search(content);
    }

    /*@PutMapping("/news/{id}")
    public News updateNote(@PathVariable(value = "id") Long newsId,
                           @Valid @RequestBody News newsDetails) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));

        news.setTitle(newsDetails.getTitle());
        news.setContent(newsDetails.getContent());

        News updatedNews = newsRepository.save(news);
        return updatedNews;
    }*/

   /* @DeleteMapping("/news/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", newsId));

        newsRepository.delete(news);

        return ResponseEntity.ok().build();
    }*/
}
