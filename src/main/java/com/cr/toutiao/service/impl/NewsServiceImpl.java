package com.cr.toutiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.News;
import com.cr.toutiao.mapper.NewsMapper;
import com.cr.toutiao.service.NewsService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-16 23:31
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<News> getLatestNews(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        if (userId != 0) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.orderByDesc("created_date");
        return newsMapper.selectList(queryWrapper);
    }

    @Override
    public int addNews(News news) {
        newsMapper.insert(news);
        return news.getId();
    }

    @Override
    public News getById(Integer newsId) {
        return newsMapper.selectById(newsId);
    }

    @Override
    public void updateCommentCount(Integer newsId, int count) {
        News news = newsMapper.selectById(newsId);
        news.setCommentCount(count);
        newsMapper.updateById(news);
    }
}
