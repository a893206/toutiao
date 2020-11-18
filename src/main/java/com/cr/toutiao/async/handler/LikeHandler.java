package com.cr.toutiao.async.handler;

import com.cr.toutiao.async.EventHandler;
import com.cr.toutiao.async.EventModel;
import com.cr.toutiao.async.EventType;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.entity.News;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.service.MessageService;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-18 23:01
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(model.getEntityOwnerId());

        User user = userService.getUser(model.getActorId());
        int newsId = model.getEntityId();
        News news = newsService.getById(newsId);
        message.setContent("用户" + user.getName() + " " + model.getExt("msg") +
                ", <a href=\"http://127.0.0.1:8080/news/" + newsId + "\">" + news.getTitle() + "</a>");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", 1, model.getEntityOwnerId()));

        System.out.println("message = " + message);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LIKE);
    }
}
