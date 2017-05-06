package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * Created by Chen on 05/05/2017.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;

    /**
     * 上传图片控制器action
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
     * @param filename String类型的文件名, 用png, jpeg, jpg结尾
     * @param response HttpServletResponse类型
     */
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void image(@RequestParam("filename") String filename, HttpServletResponse response) {
        try {
            response.setContentType("image");  //不加的话, 报错:The current request is not a multipart request
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+filename)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("图片读取错误." + e.getMessage());
        }
    }

    @RequestMapping(value = {"/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                        @RequestParam("title") String title,
                        @RequestParam("link") String link) {
        try {
            News news = new News(title, link, image);
            if (hostHolder.getUser()!=null) {
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
}
