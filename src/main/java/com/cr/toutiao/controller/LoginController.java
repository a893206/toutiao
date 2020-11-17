package com.cr.toutiao.controller;

import com.cr.toutiao.service.UserService;
import com.cr.toutiao.util.ToutiaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author cr
 * @date 2020-11-17 12:03
 */
@Slf4j
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/reg")
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.isEmpty()) {
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            log.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }
}
