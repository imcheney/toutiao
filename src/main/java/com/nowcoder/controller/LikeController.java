package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.EntityType;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Chen on 09/05/2017.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"})
    @ResponseBody
    public String like(@RequestParam("nid") int nid) {
        try {
            int uid = hostHolder.getUser().getUid();
            int likeCount = (int)likeService.addLike(uid, EntityType.NEWS, nid);
            newsService.updateLikeCount(likeCount, nid);

            eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getUid())
                    .setEntityOwnerId(newsService.getNewsByNid(nid).getUid())
                    .setEntityId(nid).setEntityType(EntityType.NEWS));  //有人点赞的时候, 才发送一个站内信;

            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            logger.error("like添加失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "like添加失败");
        }
    }

    @RequestMapping(path = {"/dislike"})
    @ResponseBody
    public String dislike(@RequestParam("nid") int nid) {
        try {
            int uid = hostHolder.getUser().getUid();
            int likeCount = (int) likeService.addDislike(uid, EntityType.NEWS, nid);
            newsService.updateLikeCount(likeCount, nid);
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            logger.error("dislike设置失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(-1, "dislike设置失败");
        }
    }
}
