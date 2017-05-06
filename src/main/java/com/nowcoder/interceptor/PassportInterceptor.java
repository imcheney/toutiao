package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Chen on 05/05/2017.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    HostHolder hostHolder;

    /**
     * 业务处理前, 如果返回false, 那就没有继续了.
     * 这里, 我们在业务处理前要做的是用户ticket的判断
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("entering preHandle interceptor...");
        String ticket = null;
        if (httpServletRequest.getCookies()!=null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        //check if ticket is valid
        if (ticket!=null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket==null || loginTicket.getStatus()!=0 || loginTicket.getExpired().before(new Date())) {
                return true;  //跳过用户设置阶段
            } else {  //用户ticket验证成功, 设置已登录效果
                User user = userDao.selectByUid(loginTicket.getUid());
                hostHolder.setUser(user);  //装入user, 实现全局访问
            }
        }
        return true;
    }

    /**
     * 在业务处理结束, view渲染之前的操作
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView!=null && hostHolder!=null) {
            //put user object into modelAndView's map, so that web page parsing phase can get it
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    /**
     * 一切(包括view的渲染)结束后的收尾
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clearUser();
    }
}
