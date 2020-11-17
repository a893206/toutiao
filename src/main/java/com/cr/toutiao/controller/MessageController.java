package com.cr.toutiao.controller;

import com.cr.toutiao.entity.HostHolder;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.entity.ViewObject;
import com.cr.toutiao.service.MessageService;
import com.cr.toutiao.service.UserService;
import com.cr.toutiao.util.ToutiaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 17:55
 */
@Slf4j
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/msg/list")
    public String conversationDetail(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> vos = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 1, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnReadCount(localUserId, msg.getConversationId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            return "letter";
        } catch (Exception e) {
            log.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @GetMapping("/msg/detail/{conversationId}")
    public String conversationDetail(Model model, @PathVariable("conversationId") String conversationId) {
        try {
            List<ViewObject> vos = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 1, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("user", user);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);

            //已读消息
            int localUserId = hostHolder.getUser().getId();
            messageService.hasRead(localUserId, conversationId);
            return "letterDetail";
        } catch (Exception e) {
            log.error("获取站内信详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @PostMapping("/msg/addMessage")
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message msg = new Message();
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setHasRead(0);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
}
