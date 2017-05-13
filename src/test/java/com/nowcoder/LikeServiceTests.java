package com.nowcoder;

import com.nowcoder.service.LikeService;
import com.nowcoder.util.EntityType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Chen on 13/05/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    //正确性测试: 预期结果与输出结果一致
    @Test
    public void likeATest() {
        System.out.println("likeATest...");
        likeService.addLike(20, EntityType.NEWS, 15);
        Assert.assertEquals(1, likeService.getLikeStatus(20, EntityType.NEWS, 15));
    }

    //异常测试: 某种错误的输入条件下, 我们期待service能抛出这个类型的异常...
    @Test(expected = IllegalArgumentException.class)
    public void likeBTest() {
        System.out.println("likeBTest...");
        throw new IllegalArgumentException();
    }

    //预先准备一些测试数据
    @Before
    public void setUp() {
        System.out.println("setUp");

    }

    //清理掉所有数据
    @After
    public void tearDown() {
        System.out.println("tearDown");
        likeService.removeLike(20, EntityType.NEWS, 15);
    }
    @BeforeClass
    public static void beforeClass() {
        System.out.println("before class===");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("after class===");
    }
}
