package com.cr.toutiao.service;

import com.cr.toutiao.entity.News;
import com.github.pagehelper.PageInfo;

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
     * @return 新闻分页对象集合
     */
    PageInfo<News> getLatestNews(int userId, int pageNum, int pageSize);

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
     *
     * @param newsId 新闻id
     * @param count  评论数量
     */
    void updateCommentCount(Integer newsId, int count);

    /**
     * 更新点赞数
     *
     * @param newsId    新闻id
     * @param likeCount 点赞数
     */
    void updateLikeCount(int newsId, int likeCount);
}
