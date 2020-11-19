package com.cr.toutiao.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.toutiao.entity.Mail;
import com.cr.toutiao.mapper.MailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * @author cr
 * @date 2020-11-19 12:24
 */
@Slf4j
@Component
public class MailSender implements InitializingBean {
    private JavaMailSenderImpl mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailMapper mailMapper;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("头条资讯");
            InternetAddress from = new InternetAddress(nick + "<931009686@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            Context context = new Context();
            context.setVariable("username", to);
            String result = templateEngine.process(template, context);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");

        String username = "931009686@qq.com";
        mailSender.setUsername(username);
        String password = mailMapper.selectOne(new QueryWrapper<Mail>().eq("username", username)).getPassword();
        mailSender.setPassword(password);

        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
