package com.cr.toutiao.async.handler;

import cn.hutool.core.text.CharSequenceUtil;
import com.cr.toutiao.async.EventHandler;
import com.cr.toutiao.async.EventModel;
import com.cr.toutiao.async.EventType;
import com.cr.toutiao.configuration.ApplicationProperties;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.entity.News;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.service.MessageService;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-18 23:01
 */
@Component
@RequiredArgsConstructor
public class LikeHandler implements EventHandler {
    private final UserService userService;
    private final NewsService newsService;
    private final MessageService messageService;
    private final ApplicationProperties applicationProperties;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(model.getEntityOwnerId());

        User user = userService.getUser(model.getActorId());
        int newsId = model.getEntityId();
        News news = newsService.getById(newsId);
        String content = CharSequenceUtil.format("用户{} {}, <a href=\"{}/news/{}\">{}</a>",
                user.getName(), model.getExt("msg"), applicationProperties.getDomain(), newsId, news.getTitle());
        message.setContent(content);
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", 1, model.getEntityOwnerId()));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LIKE);
    }
}
