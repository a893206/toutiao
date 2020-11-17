package com.cr.toutiao.controller;

import com.cr.toutiao.service.QiniuService;
import com.cr.toutiao.util.ToutiaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author cr
 * @date 2020-11-17 13:52
 */
@Slf4j
@Controller
public class NewsController {
    @Autowired
    private QiniuService qiniuService;

    @GetMapping("/image")
    @ResponseBody
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

    @PostMapping("/uploadImage")
    @ResponseBody
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
}
