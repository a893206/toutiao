package com.cr.toutiao;

import com.cr.toutiao.entity.*;
import com.cr.toutiao.mapper.MessageMapper;
import com.cr.toutiao.mapper.NewsMapper;
import com.cr.toutiao.mapper.UserMapper;
import com.cr.toutiao.service.CommentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql("/init-schema.sql")
class ToutiaoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void initData() {
        Random random = new Random();
        int n = 60;
        for (int i = 1; i <= n; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("123456");
            user.setSalt("");
            userMapper.insert(user);

            News news = new News();
            news.setCommentCount(3);
            Date date = new Date();
            date.setTime(date.getTime() - 1000 * 3600 * (100 - i));
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i);
            news.setUserId(i);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsMapper.insert(news);

            user.setPassword("newpassword");
            userMapper.updateById(user);

            // 给每个资讯插入30条评论
            for (int j = 1; j <= n / 2; j++) {
                Comment comment = new Comment();
                comment.setUserId(random.nextInt(n) + 1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("Comment" + j);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentService.addComment(comment);
            }
        }

        Assert.assertEquals("newpassword", userMapper.selectById(1).getPassword());

        Assert.assertNotNull(commentService.getCommentsByEntity(1, EntityType.ENTITY_NEWS, 1, 1).getList().get(0));
    }
}
