package com.stech.csw.crawler.news.api.Repository;


import com.stech.csw.crawler.news.api.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.title LIKE %?1%"
            + " OR n.content LIKE %?1%")
    List<News> search(String keyword);


    @Query("SELECT n FROM News n ORDER BY createdAt DESC")
    List<News> sortByCreatedTime();
}
