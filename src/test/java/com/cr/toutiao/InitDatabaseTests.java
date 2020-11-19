package com.cr.toutiao;

import com.cr.toutiao.entity.*;
import com.cr.toutiao.mapper.LoginTicketMapper;
import com.cr.toutiao.mapper.MessageMapper;
import com.cr.toutiao.mapper.NewsMapper;
import com.cr.toutiao.mapper.UserMapper;
import com.cr.toutiao.service.CommentService;
import com.cr.toutiao.util.JedisAdapter;
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
class InitDatabaseTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Test
    void initData() {
        jedisAdapter.getJedis().flushAll();

        Random random = new Random();
        int n = 60;

        User admin = new User();
        admin.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        admin.setName("系统通知");
        admin.setPassword("4C36D3A89DFB9C9BADDEEA96BFF6260D");
        admin.setSalt("e71e0");
        userMapper.insert(admin);

        for (int i = 2; i <= n; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("e71e0");
            userMapper.insert(user);
            user.setPassword("4C36D3A89DFB9C9BADDEEA96BFF6260D");
            userMapper.updateById(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setUserId(i);
            ticket.setTicket(String.format("TICKET%d", i));
            ticket.setExpired(new Date());
            ticket.setStatus(0);
            loginTicketMapper.insert(ticket);
            ticket.setStatus(2);
            loginTicketMapper.updateById(ticket);

            News news = new News();
            news.setCommentCount(3);
            Date date = new Date();
            date.setTime(date.getTime() - 1000 * 3600 * (100 - i));
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(0);
            news.setUserId(i);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsMapper.insert(news);

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

            Message message = new Message();
            message.setFromId(1);
            message.setToId(i);
            message.setContent("unread");
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(String.format("1_%d", i));
            messageMapper.insert(message);
        }

        Assert.assertEquals("4C36D3A89DFB9C9BADDEEA96BFF6260D", userMapper.selectById(1).getPassword());

        Assert.assertNotNull(loginTicketMapper.selectById(1));

        Assert.assertNotNull(newsMapper.selectById(1));

        Assert.assertNotNull(commentService.getCommentsByEntity(1, EntityType.ENTITY_NEWS, 1, 1).getList().get(0));

        Assert.assertNotNull(messageMapper.selectById(1));
    }
}
