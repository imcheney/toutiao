package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Chen on 10/05/2017.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel event) {
        System.out.println("Liked, let's add message to database...");
        Message message = new Message();
        message.setFromUid(ToutiaoUtil.SYSTEM_UID);  //目前暂时决定使用系统号给被赞的用户发信息
        message.setCreatedDate(new Date());
        int fromUid = event.getActorId();
        int toUid = event.getEntityOwnerId();
        message.setContent("用户" + (userService.getUser(fromUid)).getUsername() +  "赞了你发布的资讯, "
        + ToutiaoUtil.NEWS_URL_HEAD + event.getEntityId());
        message.setToUid(event.getEntityOwnerId());
        message.setConversationId( ToutiaoUtil.SYSTEM_UID<toUid ? ToutiaoUtil.SYSTEM_UID+"_"+toUid : toUid+"_"+ToutiaoUtil.SYSTEM_UID);
        message.setHasRead(0);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
