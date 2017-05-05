package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by Chen on 05/05/2017.
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = "toutiao_loginTicket";
    String INSERT_FIELDS = " uid, ticket, expired, status";
    String SELECT_FIELDS = " tid, " + INSERT_FIELDS;

    @Insert({"insert into " + TABLE_NAME + "(" + INSERT_FIELDS + ") " +
            "values (#{uid},#{ticket},#{expired},#{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME
    + " where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    /**
     * 函数形式参数超过一个参数的情况下, 需要使用`@Param`注解来约定参数称呼
     * @param ticket
     * @param status
     */
    @Update({"update " + TABLE_NAME + " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
