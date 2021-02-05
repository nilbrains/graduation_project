package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Userinfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserinfoMapper {

    @Insert("INSERT INTO userinfo(`id`,`uid`,`days`,`sign_day`,`pyb`,`rate`,`integral`) " +
            " VALUES(#{id},#{uid},#{days},#{signDay},#{pyb},#{rate},#{integral})")
    void save(Userinfo userinfo);

    @Select("SELECT * FROM `userinfo` WHERE `uid` = #{uid}")
    Userinfo findOneByUid(String uid);

    @Update("UPDATE `userinfo` SET `days`=#{days},`sign_day`=#{signDay},`pyb`=#{pyb}," +
            "`rate`=#{rate},`integral`=#{integral} WHERE `uid`=#{uid}")
    void update(Userinfo userinfo);

}
