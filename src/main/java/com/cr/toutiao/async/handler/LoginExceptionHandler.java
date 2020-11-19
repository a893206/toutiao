package com.cr.toutiao.async.handler;

import com.cr.toutiao.async.EventHandler;
import com.cr.toutiao.async.EventModel;
import com.cr.toutiao.async.EventType;
import com.cr.toutiao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
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

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LOGIN);
    }
}
