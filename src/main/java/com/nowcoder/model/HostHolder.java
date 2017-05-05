package com.nowcoder.model;

/**
 * Created by Chen on 05/05/2017.
 */
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
