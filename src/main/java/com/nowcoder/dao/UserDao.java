package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Chen on 03/05/2017.
 */
@Mapper
public interface UserDao {
    String TABLE_NAME = "toutiao_user";
    String INSERT_FIELDS = " username, password, salt, head_url";
    String SELECT_FIELDS = " uid, username, password, salt, head_url";

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values (#{username},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid = #{uid}"})
    User selectByUid(int uid);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where username = #{username}"})
    User selectByUsername(String username);

    @Update({"update ", TABLE_NAME, " set password = #{password} where username = #{username}"})
    void update(User user);

    @Delete({"delete from ", TABLE_NAME, " where uid = #{uid}"})
    void deleteByUid(int uid);
}
