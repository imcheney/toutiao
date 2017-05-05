package com.nowcoder.service;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import java.util.*;

import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Chen on 04/05/2017.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUser(int uid) {
        return userDao.selectByUid(uid);
    }

    /**
     * 业务1: 用户注册
     * key1: 密码的设置要+salt+md5
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgUsername", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgPassword", "密码不能为空");
            return map;
        }
        if (userDao.selectByUsername(username) != null) {
            map.put("msgUsername", "用户名已经被注册");
            return map;
        }
        User user = new User();
        user.setUsername(username);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        //set password, 可以自己再添加一些针对密码的要求
        {
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));  //取random UUID前五位
            user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        }

        userDao.addUser(user);
        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgUsername", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgPassword", "密码不能为空");
            return map;
        }
        User user = userDao.selectByUsername(username);
        if (user== null) {
            map.put("msgUsername", "用户名不存在");
            return map;
        }
        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {  //check if password match
            map.put("msgPassword", "密码错误");
            return map;
        }

        //ticket

        return map;
    }
}
