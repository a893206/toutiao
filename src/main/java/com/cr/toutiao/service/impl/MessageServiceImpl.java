package com.cr.toutiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.mapper.MessageMapper;
import com.cr.toutiao.service.MessageService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 20:59
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int addMessage(Message msg) {
        return messageMapper.insert(msg);
    }

    @Override
    public List<Message> getConversationList(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return messageMapper.getConversationList(userId);
    }

    @Override
    public List<Message> getConversationDetail(String conversationId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.orderByDesc("id");
        return messageMapper.selectList(queryWrapper);
    }

    @Override
    public int getConversationUnReadCount(int userId, String conversationId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("has_read", 0).eq("to_id", userId).eq("conversation_id", conversationId);
        return messageMapper.selectCount(queryWrapper);
    }

    @Override
    public void hasRead(int userId, String conversationId) {
        Message message = new Message();
        message.setHasRead(1);

        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", userId).eq("conversation_id", conversationId);
        messageMapper.update(message, queryWrapper);
    }
}