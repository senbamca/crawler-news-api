package com.stech.csw.crawler.news.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stech.csw.crawler.news.api.model.News;
import com.stech.csw.crawler.news.api.repository.NewsRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private NewsRepository newsRepository;

    @Ignore
    public void findAll() throws Exception {
        System.out.println("findAll() - mockMvc"+mockMvc);
       mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.news").exists())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.news[*].id").isNotEmpty());
    }

    @Test
    public void findByTrending() throws Exception {
        System.out.println("findAll() - mockMvc"+mockMvc);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news/trending")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void createNews() throws Exception {
        News news = new News();
        news.setTitle("Google India News");
        news.setContent("Google India News - Covid-19 updates");
        news.setLink("http://www.news.google.in");
        news.setRank(1);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/news")
                .content(asJsonString(news))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
