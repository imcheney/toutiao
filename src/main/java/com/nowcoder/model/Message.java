package com.nowcoder.model;

import java.util.Date;

/**
 * Created by Chen on 07/05/2017.
 */
public class Message {
    private int msgid;
    private int fromUid;
    private int toUid;
    private String content;
    private int hasRead;
    private Date createdDate;
    private String conversationId;

    public Message() {
    }

    public Message(int fromUid, int toUid, String content, int hasRead, Date createdDate, String conversationId) {
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.content = content;
        this.hasRead = hasRead;
        this.createdDate = createdDate;
        this.conversationId = conversationId;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }
}
