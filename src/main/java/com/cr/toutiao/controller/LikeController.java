package com.cr.toutiao.controller;

import com.cr.toutiao.entity.EntityType;
import com.cr.toutiao.entity.HostHolder;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.service.LikeService;
import com.cr.toutiao.service.NewsService;
import com.cr.toutiao.util.ToutiaoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "赞踩")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NewsService newsService;

    @PostMapping("/like")
    @ApiOperation("点赞")
    public String like(@Param("newId") int newsId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return ToutiaoUtil.getJSONString(1, "用户未登录");
        }
        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_NEWS, newsId);
        //更新点赞数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @PostMapping("/dislike")
    @ApiOperation("点踩")
    public String dislike(@Param("newId") int newsId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return ToutiaoUtil.getJSONString(1, "用户未登录");
        }
        long likeCount = likeService.disLike(user.getId(), EntityType.ENTITY_NEWS, newsId);
        //更新点赞数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
