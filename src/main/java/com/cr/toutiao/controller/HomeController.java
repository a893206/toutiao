package com.cr.toutiao.controller;

import com.cr.toutiao.entity.News;
import com.cr.toutiao.entity.ViewObject;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    private List<List<ViewObject>> getNews(int userId, int pageNum, int pageSize) {
        List<News> newsList = newsService.getLatestNews(userId, pageNum, pageSize);
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
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vosList", getNews(0, 1, 11));
        model.addAttribute("pop", pop);
        return "home";
    }

    @GetMapping("/user/{userId}")
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vosList", getNews(userId, 1, 10));
        return "home";
    }
}
