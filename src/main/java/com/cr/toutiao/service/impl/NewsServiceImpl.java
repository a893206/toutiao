package com.cr.toutiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.News;
import com.cr.toutiao.mapper.NewsMapper;
import com.cr.toutiao.service.NewsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-16 23:31
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public PageInfo<News> getLatestNews(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        if (userId != 0) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.orderByDesc("id");
        return new PageInfo<>(newsMapper.selectList(queryWrapper),5);
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

    @Override
    public void updateLikeCount(int newsId, int likeCount) {
        News news = newsMapper.selectById(newsId);
        news.setLikeCount(likeCount);
        newsMapper.updateById(news);
    }
}
