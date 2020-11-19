package com.cr.toutiao.async.handler;

import com.cr.toutiao.async.EventHandler;
import com.cr.toutiao.async.EventModel;
import com.cr.toutiao.async.EventType;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-19 11:57
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆IP异常");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", 1, model.getActorId()));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LOGIN);
    }
}
