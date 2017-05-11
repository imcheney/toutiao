package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Chen on 07/05/2017.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "toutiao_message";
    String INSERT_FIELDS = " from_uid, to_uid, content, has_read, created_date, conversation_id ";
    String SELECT_FIELDS = " msgid, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values ( #{fromUid}, #{toUid}, #{content}, #{hasRead}, #{createdDate}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME
    + " where conversation_id = #{conversationId} order by created_date desc"})
    List<Message> getConversationDetail(String conversationId);

    @Select({"select count(msgid)" + " from " + TABLE_NAME
            + " where conversation_id = #{conversationId} and has_read = 0"})
    int getUnreadCount(String conversationId);

    @Select({"SELECT T2.conversation_id, T2.created_date, M.from_uid, M.to_uid, M.content, T2.msgid\n" +
            "FROM \n" +
            "(SELECT conversation_id, max(created_date) created_date, COUNT(msgid) msgid FROM\n" +
            "(SELECT * FROM " + TABLE_NAME +" WHERE from_uid = #{uid} OR to_uid = #{uid} ORDER BY created_date DESC) T1 " +
            "GROUP BY conversation_id LIMIT #{offset}, #{limit}) T2 " +
            "INNER JOIN " + TABLE_NAME +" M ON T2.created_date = M.created_date " +
            "ORDER BY T2.created_date DESC;"})
    List<Message> getConversationList(@Param("uid") int uid,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

}
