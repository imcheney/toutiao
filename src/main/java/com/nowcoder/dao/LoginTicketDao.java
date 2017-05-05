package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Chen on 05/05/2017.
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = "toutiao_ticket";
    String INSERT_FIELDS = " uid, ticket, expired, status";
    String SELECT_FIELD = " tid, " + INSERT_FIELDS;

    @Insert({"insert into " + TABLE_NAME + "(" + INSERT_FIELDS + ") " +
            "values (#{uid},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket loginTicket);
}
