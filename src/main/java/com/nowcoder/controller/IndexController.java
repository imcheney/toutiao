package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Chen on 03/05/2017.
 */
//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;  //不用自己生成,SpringMVC会帮忙注入

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session) {

        return "Hello Nowcoder!" + session.getAttribute("msg") + "<br />"
                + toutiaoService.say();
    }

    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key
                          ) {
        return String.format("%s, %d, %d, %s", groupId, userId, type, key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model) {
        model.addAttribute("key1", "value1");
        List<String> colors = Arrays.asList(new String[] {"RED","BLUE", "YELLOW", "GREEN"});
        Map<String, String> map = new HashMap<String, String>();
        for (int i=0; i<5; i++) {
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("colors", colors);
        model.addAttribute("map", map);
        model.addAttribute("user", new User("Jim"));
        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuffer sbf = new StringBuffer();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String header = request.getHeader(name);
            sbf.append(name + ": " + header + "<br/>");
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie aCookie:cookies) {
            sbf.append("My Cookie: " + aCookie.getName() + ":" + aCookie.getValue());
        }
        return sbf.toString();
    }

    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(name = "nowcoderId", defaultValue = "a")String nowcoderId,
                           @RequestParam(name = "key", defaultValue = "key") String key,
                           @RequestParam(name = "value", defaultValue = "value") String value,
                           HttpServletResponse response
                           ) {
        //允许用户自己设置cookie
        response.addCookie(new Cookie(key, value));  //记住, 加Cookie的方法
        //允许用户自己设置header
        response.addHeader(key, value);
        return "NowcoderId from this cookie is:" + nowcoderId;
    }

    @RequestMapping(value = {"/redirect/{code}"})
    @ResponseBody
    public RedirectView redirect(@PathVariable("code") int code, HttpSession session) {
        RedirectView rView = new RedirectView("/", true);
        if (code == 301) {
            rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg", "jump from redirect...");
        return rView;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(name="key", required = false) String key) {
        if ("admin".equals(key)) {
            return "Hello admin!!!";
        } else {
            throw new IllegalArgumentException("Key error!");
        }
    }

    /**
     * 统一的错误处理页面, 优化用户体验.
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error " + e.getMessage();
    }
}
