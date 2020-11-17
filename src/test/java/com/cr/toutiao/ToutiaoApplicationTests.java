package com.cr.toutiao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.LoginTicket;
import com.cr.toutiao.entity.News;
import com.cr.toutiao.entity.User;
import com.cr.toutiao.mapper.LoginTicketMapper;
import com.cr.toutiao.mapper.NewsMapper;
import com.cr.toutiao.mapper.UserMapper;
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

    @Test
    void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userMapper.insert(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsMapper.insert(news);

            user.setPassword("newpassword");
            userMapper.updateById(user);

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
    }
}
