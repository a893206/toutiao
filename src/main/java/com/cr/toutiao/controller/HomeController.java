package com.cr.toutiao.controller;

import com.cr.toutiao.entity.News;
import com.cr.toutiao.entity.ViewObject;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-16 23:12
 */
@Slf4j
@Controller
public class HomeController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    private List<List<ViewObject>> getNews(List<News> newsList) {
        List<List<ViewObject>> vosList = new ArrayList<>();
        List<ViewObject> vos = new ArrayList<>();
        String prev = null;
        for (News news : newsList) {
            String curr = new SimpleDateFormat("yyyy-MM-dd").format(news.getCreatedDate());
            if (prev != null && !prev.equals(curr)) {
                vosList.add(vos);
                vos = new ArrayList<>();
            }
            add(vos, news);
            prev = curr;
        }
        if (!vos.isEmpty()) {
            vosList.add(vos);
        }
        return vosList;
    }

    private void add(List<ViewObject> vos, News news) {
        ViewObject vo = new ViewObject();
        vo.set("news", news);
        vo.set("user", userService.getUser(news.getUserId()));
        vos.add(vo);
    }

    @GetMapping({"/", "/index"})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop,
                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<News> pageInfo = newsService.getLatestNews(0, pageNum, pageSize);
        model.addAttribute("vosList", getNews(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pop", pop);
        return "home";
    }

    @GetMapping("/user/{userId}")
    public String userIndex(Model model, @PathVariable("userId") int userId,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<News> pageInfo = newsService.getLatestNews(userId, pageNum, pageSize);
        model.addAttribute("vosList", getNews(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
