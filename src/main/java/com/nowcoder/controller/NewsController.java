package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.EntityType;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by Chen on 05/05/2017.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/news/{nid}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("nid") int nid, Model model) {
        News news = newsService.getNewsByNid(nid);
        if (news!=null) {
            //comment
            List<Comment> commentList = commentService.selectCommentByEntity(news.getNid(), EntityType.NEWS);
            //此处需要使用viewobject来包装comment对象和对应的user对象
            List<ViewObject> vos = new ArrayList<ViewObject>();
            for (Comment comment:commentList) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUid()));
                vos.add(vo);
            }
            model.addAttribute("comments", vos);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUid()));
        return "detail";
    }

    @RequestMapping(value = {"/addNews", "/addNews/", "/user/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News(title, link, image);
            if (hostHolder.getUser() != null) {
                news.setUid(hostHolder.getUser().getUid());
            } else {  //set an anonymous user
                news.setUid(3);
            }
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0, "发布成功");
        } catch (Exception e) {
            logger.error("添加新闻异常: " + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "发布失败");
        }
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int nid,
                             @RequestParam("content")String content) {
        try {
            //filter content
            Comment comment = new Comment(hostHolder.getUser().getUid(), nid, 1, content,  new Date(), 0);
            commentService.addComment(comment);
            System.out.println("comment: " + comment.getUid());
            int count = commentService.getCommentCountByEntity(nid, 1);
            newsService.updateCommentCount(count, nid);
        } catch (Exception e) {
            logger.error("评论添加失败 " + e.getMessage());
        }
        return "redirect: /news/" + nid;
    }

    /**
     * 上传图片控制器action
     *
     * @param file
     * @return
     */
    @RequestMapping(path = {"/uploadImage"}, method = {RequestMethod.POST})
    @ResponseBody  //responseBody类型要求返回的是文本类型, 作为纯文本显示在页面上.
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//            String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl != null) {
                return ToutiaoUtil.getJSONString(0, fileUrl);
            } else {
                return ToutiaoUtil.getJSONString(-1, "上传失败");
            }
        } catch (Exception e) {
            logger.error("服务器保存图片错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "上传失败");
        }
    }

    /**
     * 获取image的action
     *
     * @param filename String类型的文件名, 用png, jpeg, jpg结尾
     * @param response HttpServletResponse类型
     */
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void image(@RequestParam("filename") String filename, HttpServletResponse response) {
        try {
            response.setContentType("image");  //不加的话, 报错:The current request is not a multipart request
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + filename)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("图片读取错误." + e.getMessage());
        }
    }

}
