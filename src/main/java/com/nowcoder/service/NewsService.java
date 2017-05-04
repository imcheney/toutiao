package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen on 04/05/2017.
 */
@Service
public class NewsService {
    @Autowired
    NewsDao newsDao;

    public List<News> getLatestNews(int uid, int offset, int limit) {
        return newsDao.selectByUidAndOffset(uid, offset, limit);
    }
}
