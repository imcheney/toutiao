package com.nowcoder.service;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen on 07/05/2017.
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public void addMessage(Message message) {
        messageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId) {
        return messageDao.getConversationDetail(conversationId);
    }

    public List<Message> getConversationList(int uid, int offset, int limit) {
        return messageDao.getConversationList(uid, offset, limit);
    }

    public int getUnreadCount(String conversationId) {
        return messageDao.getUnreadCount(conversationId);
    }
}
