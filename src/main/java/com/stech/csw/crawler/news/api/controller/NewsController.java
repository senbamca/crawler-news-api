package com.stech.csw.crawler.news.api.controller;


import com.stech.csw.crawler.news.api.Repository.NewsRepository;
import com.stech.csw.crawler.news.api.exception.ResourceNotFoundException;
import com.stech.csw.crawler.news.api.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/notes")
    public List<News> getAllNotes() {
        return newsRepository.findAll();
    }

    @PostMapping("/notes")
    public News createNote(@Valid @RequestBody News news) {
        return newsRepository.save(news);
    }

    @GetMapping("/notes/{id}")
    public News getNoteById(@PathVariable(value = "id") Long noteId) {
        return newsRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    @PutMapping("/notes/{id}")
    public News updateNote(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody News newsDetails) {

        News news = newsRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        news.setTitle(newsDetails.getTitle());
        news.setContent(newsDetails.getContent());

        News updatedNews = newsRepository.save(news);
        return updatedNews;
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        News news = newsRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        newsRepository.delete(news);

        return ResponseEntity.ok().build();
    }
}
