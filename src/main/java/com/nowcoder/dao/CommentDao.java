package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Chen on 07/05/2017.
 */

@Mapper
public interface CommentDao {
    String TABLE_NAME = "toutiao_comment";
    String INSERT_FIELDS = " uid, entity_id, entity_type, content, created_date, status ";
    String SELECT_FIELDS = " cid, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values ( #{uid}, #{entityId}, #{entityType}, #{content}, #{createdDate}, #{status})"})
        //此处注意likeCount的camelCase写法, 这是因为`#{likeCount}`是从news对象中提取属性的
    int addComment(Comment comment);

    /**
     * really delete the record in database, be careful to use this.
     * @param cid
     */
    @Delete({"delete from ", TABLE_NAME, "where cid = #{cid}"})
    void deleteByCid(int cid);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where cid = #{cid} and status = 0"})
    Comment selectByCid(int cid);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME
            + " where entity_type = #{entityType} and entity_id = #{entityId} and status = 0 "
            + " order by created_date desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * get commentCount for an entity
     * this only show those comments having status of 0
     * @param entityId
     * @param entityType
     * @return commentCount number, int
     */
    @Select({"select count(cid) from " + TABLE_NAME
            + " where entity_type = #{entityType} and entity_id = #{entityId} and status = 0"})
    int getCommentCountByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * update comment status, you can choose 0 or -1
     * usually use it to delete a comment
     * @param cid
     * @param status
     */
    @Update({"update " + TABLE_NAME + " set status = #{status} where cid = #{cid}"})
    void updateStatus(@Param("cid") int cid, @Param("status") int status);
}
