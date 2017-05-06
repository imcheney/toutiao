package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by Chen on 05/05/2017.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> onSiteUser = new ThreadLocal<User>();

    /**
     * get onSiteUser
     * @return
     */
    public User getUser() {
        return onSiteUser.get();
    }

    /**
     * set up a thread-local  on site user
     * @param user
     */
    public void setUser(User user) {
        onSiteUser.set(user);
    }

    /**
     * clear onSiteUser
     */
    public void clearUser() {
        onSiteUser.remove();
    }
}
