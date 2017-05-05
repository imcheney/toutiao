package com.nowcoder;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.NewsDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@Sq l("/init-schema.sql ")
public class InitDataBaseTests {
    @Autowired
    UserDao userDao;

    @Autowired
    NewsDao newsDao;

    @Autowired
    LoginTicketDao loginTicketDao;

	@Test
	public void contextLoads() {
        //testUser();
        //testNews();
        //testLoginTicket();
        loginTicketDao.updateStatus("Ticket3", -1);
        Assert.assertEquals(loginTicketDao.selectByTicket("Ticket3").getStatus(), -1);
    }

    private void testLoginTicket() {
        for (int i=1; i<=10; i++) {
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUid(i+1);
            loginTicket.setTicket(String.format("Ticket%d", i));
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*24*7);
            loginTicket.setExpired(date);
            loginTicket.setStatus(0);
            loginTicketDao.addLoginTicket(loginTicket);
        }
    }

    private void testNews() {
        for (int i=1; i<=10; i++) {
            News news = new News();
            news.setTitle(String.format("Headline-%d", i));
            news.setLink(String.format("http://news.nowcoder.com/%d", i));
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", i));
            news.setLikeCount(i*10);
            news.setCommentCount(i*3);
            Date date = new Date();
            date.setTime(date.getTime() + 12*1000*3600*i);  //每个新闻间隔12个小时
            news.setCreatedDate(date);
            news.setUid(i+1);

            newsDao.addNews(news);
        }
    }

    private void testUser() {
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            User user = new User(String.format("USER%d",i), "", "",
                    String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            userDao.addUser(user);

            user.setPassword("1234");
            userDao.update(user);
        }
        Assert.assertEquals("987654321",userDao.selectByUid(1).getPassword());
        userDao.deleteByUid(1);
        Assert.assertNull(userDao.selectByUid(1));
    }
}
