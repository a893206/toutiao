package com.cr.toutiao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.*;
import com.cr.toutiao.mapper.LoginTicketMapper;
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
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private CommentService commentService;

    @Test
    void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i + 1));
            user.setPassword("");
            user.setSalt("");
            userMapper.insert(user);

            News news = new News();
            news.setCommentCount(3);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i + 1));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i + 1));
            newsMapper.insert(news);

            user.setPassword("newpassword");
            userMapper.updateById(user);

            // 给每个资讯插入3个评论
            for (int j = 1; j <= 3; ++j) {
                Comment comment = new Comment();
                comment.setUserId(i + 1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("Comment" + j);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentService.addComment(comment);
            }

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i + 1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i + 1));
            loginTicketMapper.insert(ticket);

            ticket.setStatus(2);
            loginTicketMapper.updateById(ticket);
        }

        Assert.assertEquals("newpassword", userMapper.selectById(1).getPassword());

        LoginTicket loginTicket = loginTicketMapper.selectOne(new QueryWrapper<LoginTicket>().eq("ticket", "TICKET1"));
        Assert.assertEquals((Integer) 1, loginTicket.getUserId());
        Assert.assertEquals((Integer) 2, loginTicket.getStatus());

        Assert.assertNotNull(commentService.getCommentsByEntity(1, EntityType.ENTITY_NEWS).get(0));
    }
}
