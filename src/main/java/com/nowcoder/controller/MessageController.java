package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chen on 07/05/2017.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId")String fromId,
                           @RequestParam("toId")String toId,
                           @RequestParam("content")String content) {
        try {
            int fromUId = Integer.parseInt(fromId);
            int toUid = Integer.parseInt(toId);
            String conversationId = (fromUId<toUid)?String.format("%d_%d", fromUId, toUid):String.format("%d_%d", toUid, fromUId);
            Message message = new Message(fromUId, toUid, content, 0, new Date(), conversationId);
            System.out.println(message.getContent() + message.getConversationId());
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0, "sent message successfully");
        } catch (Exception e) {
            logger.error("添加Message失败" + e.getMessage());
            e.printStackTrace();
            return ToutiaoUtil.getJSONString(-1, "添加Message失败");

        }
    }

    @RequestMapping(path = {"/msg/detail?conversationId}", "msg/detail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String conversationDetail(@RequestParam("conversationId") String conversationId, Model model) {
        try {
            List<Message> msgList = messageService.getConversationDetail(conversationId);
            List<ViewObject> messageVos = new ArrayList<ViewObject>();
            for (Message message:msgList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("fromUser", userService.getUser(message.getFromUid()));
                messageVos.add(vo);
            }
            model.addAttribute("messageVos", messageVos);
        } catch (Exception e) {
            logger.error("查询conversation失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list", "/msg/list/"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUid = hostHolder.getUser().getUid();
            List<Message> convList = messageService.getConversationList(localUid, 0, 10);
            List<ViewObject> convVos = new ArrayList<ViewObject>();
            System.out.println("size: " + convList.size());

            for (Message conv:convList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", conv);
                vo.set("unreadCount", messageService.getUnreadCount(conv.getConversationId()));
                User anotherUser = userService.getUser(conv.getFromUid()==localUid?conv.getToUid():conv.getFromUid());
                vo.set("anotherUser", anotherUser);
                convVos.add(vo);
                System.out.println(String.format("%d", conv.getMsgid()));
            }
            model.addAttribute("convVos", convVos);
        } catch (Exception e) {
            logger.error("查询conversation list失败" + e.getMessage());
        }
        return "letter";
    }
}
