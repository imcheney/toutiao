package com.nowcoder.service;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
