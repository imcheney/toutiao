package com.nowcoder;

import com.nowcoder.service.LikeService;
import com.nowcoder.util.EntityType;
import com.nowcoder.util.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Chen on 09/05/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeTest {
    @Autowired
    LikeService likeService;

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Test
    public void contextLoads() {
        System.out.println(likeService.addLike(23, 1, 18));
//        jedisAdaptor.sadd(RedisKeyUtil.getLikeKey(EntityType.NEWS, 18), "23");
    }
}
