package com.stech.csw.crawler.news.api.Repository;


import com.stech.csw.crawler.news.api.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

}
