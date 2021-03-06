package com.nowcoder.controller;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 04/05/2017.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getNews(int uid, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(uid, offset, limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        int localUserId = hostHolder.getUser()!=null? hostHolder.getUser().getUid() : -1;
        for (News aNews:newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", aNews);
            vo.set("user", userService.getUser(aNews.getUid()));
            if (localUserId!=-1){  //用户已经登录
                int likeStatus = likeService.getLikeStatus(localUserId, EntityType.NEWS, aNews.getNid());
                vo.set("like", likeStatus);  //设置喜好状态
                logger.info("已经设置好了like的状态信息, like value: " + likeStatus);
            } else {
                vo.set("like", 0);  //0为无喜好
            }
            vos.add(vo);
        }
        return vos;
    }
    /**
     * 这种index指向某个网页的, 不要加ResponseBody,
     * 那个代表的是return后面的部分是text类型的body.
     * 另外, 这里使用VOS, 也就是说VO类来装其他所有要传的
     * @param model
     * @return
     */
    @RequestMapping(path = {"/", "/index"})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/userIndex/{uid}", "/user/{uid}"})
    public String userIndex(Model model, @PathVariable("uid") int uid) {
        model.addAttribute("vos", getNews(uid, 0, 10));
        return "home";
    }

}
