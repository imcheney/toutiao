package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.MailSender;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Chen on 11/05/2017.
 */
@Component
public class LoginExceptionHandler implements EventHandler{
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel event) {
        logger.info("===>" + event.getExt("datetime"));
        Message message = new Message();
        message.setFromUid(ToutiaoUtil.SYSTEM_UID);  //登录异常由信息发给账户异常用户;
        message.setToUid(event.getEntityOwnerId());
        User affectedUser = userService.getUser(event.getEntityOwnerId());
        System.out.println("==============" + event.getExt("datetime"));
        message.setContent( "[系统提示] " + affectedUser.getUsername() + ",您的账户于" + event.getExt("datetime") + "发生登录异常, 异常信息是: " + event.getExt("ExceptionInfo") + ". 请您注意账号安全!");
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        message.setConversationId(ToutiaoUtil.SYSTEM_UID<event.getEntityOwnerId()?
                ToutiaoUtil.SYSTEM_UID+"_"+event.getEntityOwnerId():event.getEntityOwnerId()+"_"+ToutiaoUtil.SYSTEM_UID);
        messageService.addMessage(message);
        Map<String, Object> mailInfoMap = new HashMap<String, Object>();
        mailInfoMap.put("username", affectedUser.getUsername());
        mailSender.sendWithHTMLTemplate(ToutiaoUtil.TEST_MAIL, "账户异常提醒", "mails/loginException.html", mailInfoMap);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
