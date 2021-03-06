package com.nowcoder.service;

import com.nowcoder.controller.LikeController;
import com.nowcoder.util.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Like业务相关
 * Created by Chen on 09/05/2017.
 */
@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    JedisAdaptor jedisAdaptor;

    /**
     * 返回用户对某条新闻的喜欢状态
     * @param uid
     * @param entityType
     * @param entityId
     * @return 喜欢:1, 讨厌:-1, 无:0
     */
    public int getLikeStatus(int uid, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(likeKey, String.valueOf(uid))) {  //uid in set(LIKE:1:nid)
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(dislikeKey, String.valueOf(uid))) {
            return -1;
        }
        return 0;
    }

    public long addLike(int uid, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        try {
            long res = jedisAdaptor.sadd(likeKey, String.valueOf(uid));  //添加likeKey
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId); //删除dislikeKey
        jedisAdaptor.srem(dislikeKey, String.valueOf(uid));
        return jedisAdaptor.scard(likeKey);  //返回likeCount
    }

    public long addDislike(int uid, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);  //添加dislikeKey
        long res = jedisAdaptor.sadd(dislikeKey, String.valueOf(uid));
        jedisAdaptor.srem(likeKey, String.valueOf(uid));  //删除喜欢
        return jedisAdaptor.scard(likeKey);  //无论添加like or dislike, 返回在页面上显示的都是likeCount
    }

    public void removeLike(int uid, int entityType, int entityId) {
        jedisAdaptor.srem(RedisKeyUtil.getLikeKey(entityType, entityId), String.valueOf(uid));
    }

    public void removeDislike(int uid, int entityType, int entityId) {
        jedisAdaptor.srem(RedisKeyUtil.getDislikeKey(entityType, entityId), String.valueOf(uid));
    }
}
