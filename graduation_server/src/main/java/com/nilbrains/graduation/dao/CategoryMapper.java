package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {

    @Insert("INSERT INTO `category`(`cid`,`title`,`status`) VALUES (#{cid},#{title},#{status})")
    void save(Category category);

    @Select("SELECT * FROM `category` WHERE `cid`=#{cid}")
    Category findOneByCid(String cid);

    @Select("SELECT * FROM `category` WHERE `title`=#{title}")
    Category findOne(String title);

    @Update("UPDATE `category` SET `title`=#{title} WHERE `cid`=#{cid}")
    void update(Category category);

    @Update("UPDATE `category` SET `status`=(`status`+1)%2 WHERE `cid`=#{cid}")
    void changeStatus(String cid);

    @Select("<script> SELECT count(*) FROM `category`" +
            "<where> <if test='status != null and status != &quot;&quot;'>" +
            " and `status` = #{status} " +
            "</if> </where> </script>"
    )
    int getCount(String status);

    @Select("<script> SELECT * FROM `category`" +
            "<where> <if test='status != null and status != &quot;&quot;'>" +
            " and `status` = #{status} " +
            "</if> </where> </script>"
    )
    List<Category> getCategories(String status);
}
