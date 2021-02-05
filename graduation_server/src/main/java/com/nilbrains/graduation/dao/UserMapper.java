package com.nilbrains.graduation.dao;


import com.nilbrains.graduation.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `user`(`uid`,`account`,`password`,`role`,`name`,`sign`,`avatar`,`status`) " +
            "VALUES (#{uid},#{account},#{password},#{role},#{name},#{sign},#{avatar},#{status})")
    void saveUser(User user);

    @Select("SELECT * FROM `user` WHERE `account` = #{account}")
    User findUserByAccount(String account);

    @Select("SELECT `u`.`uid`,`u`.`account`,`u`.`role`,`u`.`name`,`u`.`sign`,`u`.`avatar`,`u`.`status`,`ui`.`rate`" +
            " FROM `user` `u`,`userinfo` `ui` WHERE `ui`.`uid` = `u`.`uid` AND `u`.`uid` = #{uid}")
    User getUserByUid(String uid);

    @Update("UPDATE `user` SET `name`=#{name},`sign`=#{sign},`avatar`=#{avatar} WHERE `uid`=#{uid}")
    void updateUser(User user);

    @Update("UPDATE `user` SET `password`=#{password} WHERE `uid`=#{uid}")
    void updatePass(User user);

    @Select("<script>SELECT count(*) FROM `user` " +
            "<where> <if test='name != null and name != &quot;&quot;'> " +
            " and `name` like concat('%',#{name},'%') " +
            "</if> </where> </script>")
    int getCount(String name);

    @Select("<script>SELECT * FROM `user` " +
            "<where> <if test='name != null and name != &quot;&quot;'> " +
            " and `name` like concat('%',#{name},'%') " +
            "</if> </where>" +
            " LIMIT #{limit},#{offset} </script>")
    List<User> getUsers(String name, int limit, int offset);

    @Update("UPDATE `user` SET `status`=(`status`+1)%2 WHERE `uid`=#{uid}")
    void updateStatus(String uid);
}
