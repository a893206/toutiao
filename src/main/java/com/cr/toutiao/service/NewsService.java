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

    /**
     * 添加新闻
     *
     * @param news 新闻对象
     * @return 添加结果
     */
    int addNews(News news);

    /**
     * 根据新闻id查询新闻
     *
     * @param newsId 新闻id
     * @return 新闻对象
     */
    News getById(Integer newsId);

    /**
     * 更新评论数量
     * @param newsId 新闻id
     * @param count 评论数量
     */
    void updateCommentCount(Integer newsId, int count);
}
