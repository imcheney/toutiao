package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Chen on 05/05/2017.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户注册Controller
     * @param model
     * @param username
     * @param password
     * @param response
     * @return json String
     */
    @RequestMapping(path = {"/register"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(name = "rember", defaultValue = "0") String rememberMe,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map =  userService.register(username, password);
            if (map.isEmpty()) {
                return ToutiaoUtil.getJSONString(0, "用户注册成功");
            } else {
                return ToutiaoUtil.getJSONString(-1, map);
            }
        } catch (Exception e) {
            logger.error("注册出错" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "用户注册异常");
        }
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(name = "rember", defaultValue = "0") String rememberMe,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map =  userService.register(username, password);
            if (map.isEmpty()) {
                return ToutiaoUtil.getJSONString(0, "用户注册成功");
            } else {
                return ToutiaoUtil.getJSONString(-1, map);
            }
        } catch (Exception e) {
            logger.error("注册出错" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "用户注册异常");
        }
    }

}
