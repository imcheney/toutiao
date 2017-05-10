package com.nowcoder;

import com.nowcoder.model.User;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.JedisAdaptor;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Chen on 10/05/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class JedisTest {
    @Autowired
    JedisAdaptor jedisAdaptor;

    @Test
    public void contextLoads() {
//        User user1001 = new User();
//        user1001.setUsername("JerryBrown");
//        user1001.setPassword("9876");
//        user1001.setHeadUrl("www.163.com");
//        jedisAdaptor.setObject("user1001", user1001);
        User user = jedisAdaptor.getObject("user1001", User.class);
        System.out.println(ToStringBuilder.reflectionToString(user));
    }
}