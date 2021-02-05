package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ImageMapper {

    @Insert("INSERT INTO `image`(`id`,`uid`,`name`,`path`,`url`,`status`,`content_type`,`create_time`) " +
            "VALUES(#{id},#{uid},#{name},#{path},#{url},#{status},#{contentType},#{createTime})")
    void save(Image image);

    @Select("SELECT `path`,`content_type` FROM `image` WHERE `id` = #{imageIdDb}")
    Image getSimpleOne(String imageIdDb);
}
