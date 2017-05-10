package com.nowcoder.util;

/**
 * redis存储的规范化
 * 统一的redis key生成
 * biz+name
 * Created by Chen on 09/05/2017.
 */
public class RedisKeyUtil {  //util类一般都使用静态方法, 因此成员属性也得是静态的
    private static String SEPARATOR = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SEPARATOR + String.valueOf(entityType) + SEPARATOR + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SEPARATOR + String.valueOf(entityType) + SEPARATOR + String.valueOf(entityId);
    }



}
