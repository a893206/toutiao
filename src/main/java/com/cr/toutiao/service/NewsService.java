package com.cr.toutiao.service;

import com.cr.toutiao.entity.News;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-16 23:21
 */
public interface NewsService {
    /**
     * 获取最近新闻
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return 新闻对象集合
     */
    List<News> getLatestNews(int userId, int pageNum, int pageSize);
}
