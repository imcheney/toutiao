package com.nowcoder;

import com.nowcoder.service.LikeService;
import com.nowcoder.util.JedisAdaptor;
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
public class EventTest {
    @Autowired
    LikeService likeService;

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Test
    public void contextLoads() {

    }
}