package com.cr.toutiao.service;

import com.cr.toutiao.entity.Comment;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 16:54
 */
public interface CommentService {
    /**
     * 添加评论
     *
     * @param comment 评论对象
     * @return 添加结果
     */
    int addComment(Comment comment);

    /**
     * 根据实体类型获取评论
     *
     * @param entityId   实体类型id
     * @param entityType 实体类型
     * @return 评论对象集合
     */
    List<Comment> getCommentsByEntity(int entityId, int entityType);

    /**
     * 获取评论数量
     *
     * @param entityId   实体类型id
     * @param entityType 实体类型
     * @return 评论数量
     */
    int getCommentCount(int entityId, int entityType);
}
