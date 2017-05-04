package com.nowcoder.controller;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 04/05/2017.
 */
@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    /**
     * 这种index指向某个网页的, 不要加ResponseBody,
     * 那个代表的是return后面的部分是text类型的body.
     * 另外, 这里使用VOS, 也就是说VO类来装其他所有要传的
     * @param model
     * @return
     */
    @RequestMapping(path = {"/", "/index"})
    public String index(Model model) {
        List<News> newsList = newsService.getLatestNews(0, 0, 10);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News aNews:newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", aNews);
            vo.set("user", userService.getUser(aNews.getUid()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "home";
    }

}
