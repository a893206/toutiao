package com.cr.toutiao.controller;

import com.cr.toutiao.entity.*;
import com.cr.toutiao.service.*;
import com.cr.toutiao.util.ToutiaoUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cr
 * @date 2020-11-17 13:52
 */
@Slf4j
@Controller
@Api(tags = "资讯")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/uploadImage")
    @ResponseBody
    @ApiOperation("上传图片")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            log.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    @GetMapping("/image")
    @ResponseBody
    @ApiOperation("查看图片")
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(QiniuService.QINIU_IMAGE_DOMAIN + imageName)), response.getOutputStream());
        } catch (Exception e) {
            log.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @GetMapping("/news/{newsId}")
    @ApiOperation("资讯详情")
    public String newsDetail(@PathVariable("newsId") int newsId, Model model,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        News news = newsService.getById(newsId);
        if (news != null) {
            User user = hostHolder.getUser();
            if (user != null) {
                model.addAttribute("like", likeService.getLikeStatus(user.getId(), EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            //评论
            PageInfo<Comment> pageInfo = commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS, pageNum, pageSize);
            List<Comment> commentList = pageInfo.getList();
            List<ViewObject> vos = new ArrayList<>();
            for (Comment comment : commentList) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));

        return "detail";
    }

    @PostMapping("/user/addNews")
    @ResponseBody
    @ApiOperation("添加资讯")
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            news.setLikeCount(0);
            news.setCommentCount(0);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            log.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @PostMapping("/addComment")
    @ApiOperation("添加评论")
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {

        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            log.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + newsId;
    }
}
