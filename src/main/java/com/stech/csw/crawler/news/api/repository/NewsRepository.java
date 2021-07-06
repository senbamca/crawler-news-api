package com.stech.csw.crawler.news.api.repository;


import com.stech.csw.crawler.news.api.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.title LIKE %?1%"
            + " OR n.content LIKE %?1%"
            + " OR n.link LIKE %?1% ORDER BY createdAt DESC")
    List<News> search(String keyword);

    @Query("SELECT n FROM News n WHERE n.rank = ?1 ORDER BY rank DESC")
    List<News> findByRank(Integer rank);

    News findByTitleAndLink(String title, String link);
}
