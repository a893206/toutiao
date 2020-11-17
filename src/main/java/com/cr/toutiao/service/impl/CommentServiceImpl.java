package com.cr.toutiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.Comment;
import com.cr.toutiao.mapper.CommentMapper;
import com.cr.toutiao.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 16:58
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_id", entityId).eq("entity_type", entityType);
        queryWrapper.orderByDesc("id");
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_id", entityId).eq("entity_type", entityType);
        return commentMapper.selectCount(queryWrapper);
    }
}
