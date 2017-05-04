package com.nowcoder.model;

/**
 * Created by Chen on 03/05/2017.
 */
public class User {
    private int uid;
    private String username;
    private String password;
    private String salt;
    private String headUrl;  //头像URL

    public User(String username, String password, String salt, String headUrl) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.headUrl = headUrl;
    }

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
