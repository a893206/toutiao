package com.cr.toutiao.controller;

import com.cr.toutiao.entity.HostHolder;
import com.cr.toutiao.entity.Message;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.entity.ViewObject;
import com.cr.toutiao.service.MessageService;
import com.cr.toutiao.service.UserService;
import com.cr.toutiao.util.ToutiaoUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "站内信")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/msg/list")
    @ApiOperation("站内信列表")
    public String conversationDetail(Model model,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> vos = new ArrayList<>();
            PageInfo<Message> pageInfo = messageService.getConversationList(localUserId, pageNum, pageSize);
            List<Message> conversationList = pageInfo.getList();
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
            model.addAttribute("pageInfo", pageInfo);
            return "letter";
        } catch (Exception e) {
            log.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @GetMapping("/msg/detail/{conversationId}")
    @ApiOperation("会话详情")
    public String conversationDetail(Model model, @PathVariable("conversationId") String conversationId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            List<ViewObject> vos = new ArrayList<>();
            PageInfo<Message> pageInfo = messageService.getConversationDetail(conversationId, pageNum, pageSize);
            List<Message> conversationList = pageInfo.getList();
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
            model.addAttribute("pageInfo", pageInfo);

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
    @ApiOperation("发送站内信")
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
