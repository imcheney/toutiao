package com.nowcoder.controller;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.service.UserService;
import com.nowcoder.util.EntityType;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Chen on 05/05/2017.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private EventProducer eventProducer;

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
                           @RequestParam(name = "rember", defaultValue = "0") int rememberMe,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map =  userService.register(username, password);
            if (map.containsKey("ticket")) {
                //设置coockie
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());  //what
                cookie.setPath("/");  //where
                if (rememberMe > 0) {
                    cookie.setMaxAge(3600 * 24 * 7);  //when
                }
                response.addCookie(cookie);
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
                           @RequestParam(name = "rember", defaultValue = "0") int rememberMe,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map =  userService.login(username, password);
            if (map.containsKey("ticket")) {
                //设置coockie
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());  //what
                cookie.setPath("/");  //where
                if (rememberMe > 0) {  //用户登录的时候要求rememberMe
                    cookie.setMaxAge(3600 * 24 * 7);  //when
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "用户登录成功");
            } else {
                String msgType = map.get("msgType").toString();
                String ExceptionInfo = (String)map.get(msgType);
                EventModel event = (new EventModel(EventType.LOGIN)).setEntityOwnerId((int)map.get("uid"))
                        .setEntityType(EntityType.ACCOUNT)
                        .setExt("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                        .setExt("ExceptionInfo", ExceptionInfo);
                logger.info(event.getExt("datetime"));
                logger.info(event.getExt("ExceptionInfo"));
                eventProducer.fireEvent(event);  //有一个常驻的consumer线程来负责分发这些event;
                return ToutiaoUtil.getJSONString(-1, map);
            }
        } catch (Exception e) {
            logger.error("登录出错" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "用户登录异常");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket")String ticket) {  //get ticket from cookies out of request
        userService.logout(ticket);
        return "redirect:/";
    }

}
