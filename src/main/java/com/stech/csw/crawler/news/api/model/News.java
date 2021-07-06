package com.stech.csw.crawler.news.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "createdBy", "updatedBy"},
        allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition="TEXT")
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String link;

    @NotBlank
    private String source;

    @Lob
    @Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(updatable = false, columnDefinition="varchar(6) default 'API'")
    private String createdBy;

    @Column(columnDefinition="varchar(6) default 'API'")
    private String updatedBy;

    public News() {
        this.source = "WEB";
        this.createdBy = "API";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLink() {return link;}

    public void setLink(String link) {this.link = link;}

    public String getSource() {return source;}

    public void setSource(String source) {this.source = source;}

    public String getCreatedBy() {return createdBy;}

    public void setCreatedBy(String name){this.createdBy = name;}

    public void setUpdatedBy(String name){this.updatedBy = name;}

    public String getUpdatedBy() {return updatedBy;}

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
