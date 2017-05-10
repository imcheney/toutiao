package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Chen on 04/05/2017.
 */
@Mapper
public interface NewsDao {
    String TABLE_NAME = "toutiao_news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, uid ";
    String SELECT_FIELDS = " nid, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values (#{title}, #{link}, #{image}, #{likeCount}, #{commentCount}, #{createdDate}, #{uid})"})
    //此处注意likeCount的camelCase写法, 这是因为`#{likeCount}`是从news对象中提取属性的
    int addNews(News news);

    @Delete({"delete from ", TABLE_NAME, "where nid = #{nid}"})
    void deleteByNid(int nid);

    /**
     * 通过uid, offset, limit来进行新闻的查询
     * 借助xml实现, 其中#{uid}等由于有大于等于两个参数, 需要在形参列表中进行@Param声明
     * @param uid
     * @param offset
     * @param limit
     * @return
     */
    List<News> selectByUidAndOffset(@Param("uid") int uid, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where nid = #{nid}"})
    News selectByNid(int nid);

    @Update({"update " + TABLE_NAME + " set comment_count = #{count} where nid = #{nid}"})
    void updateCommentCount(@Param("count") int count, @Param("nid") int nid);

    @Update({"update " + TABLE_NAME + " set like_count = #{likeCount} where nid = #{nid}"})
    void updateLikeCount(@Param("likeCount")int likeCount, @Param("nid") int nid);
}
