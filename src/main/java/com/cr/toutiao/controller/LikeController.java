package com.cr.toutiao.controller;

import com.cr.toutiao.entity.EntityType;
import com.cr.toutiao.entity.HostHolder;
import com.cr.toutiao.service.LikeService;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.util.ToutiaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cr
 * @date 2020-11-18 16:18
 */
@Slf4j
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NewsService newsService;

    @PostMapping("/like")
    public String like(@Param("newId") int newsId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        //更新点赞数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @PostMapping("/dislike")
    public String dislike(@Param("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        //更新点赞数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
