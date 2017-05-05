package com.nowcoder.model;

import java.util.Date;

/**
 * Created by Chen on 05/05/2017.
 */
public class LoginTicket {
    int tid;
    int uid;
    String ticket;
    Date expired;
    int status;

    public LoginTicket() {
    }

    public LoginTicket(int uid, String ticket, Date expired, int status) {
        this.uid = uid;
        this.ticket = ticket;
        this.expired = expired;
        this.status = status;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
