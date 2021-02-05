package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Comment;
import com.nilbrains.graduation.response.CommentWithUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO `comment`(`id`,`uid`,`content`,`pid`,`tid`,`is_true`,`publish_time`) " +
            "VALUES(#{id},#{uid},#{content},#{pid},#{tid},#{isTrue},#{publishTime})")
    void save(Comment comment);

    @Delete("DELETE FROM `comment` WHERE `id` = #{id}")
    void delete(String id);

    @Select("SELECT * FROM `comment` WHERE `pid` = #{pid} AND `tid` is null ORDER BY `publish_time` DESC LIMIT #{limit},#{offset}")
    @Results(id = "CommentWithUser", value = {
            @Result(column = "uid", property = "user",
                    one = @One(select = "com.nilbrains.graduation.dao.UserMapper.getUserByUid")),
    })
    List<CommentWithUser> findCommentsByPid(String pid, int limit, int offset);


    @Select("SELECT * FROM `comment` ORDER BY `publish_time` DESC LIMIT #{limit},#{offset}")
    @ResultMap("CommentWithUser")
    List<CommentWithUser> findComments(int limit, int offset);

    @Select("SELECT * FROM `comment` WHERE `tid` = #{tid} ORDER BY `publish_time` DESC")
    @ResultMap("CommentWithUser")
    List<CommentWithUser> findCommentsByTid(String tid);

    @Select("SELECT count(*) FROM `comment` WHERE `pid` = #{pid}")
    int getCount(String pid);

    @Select("SELECT count(*) FROM `comment`")
    int getCountAll();

    @Update("UPDATE `comment` SET `is_true` = #{isTrue} WHERE `id` = #{id}")
    void updateTrue(String id, String isTrue);

    @Delete("DELETE FROM `comment` WHERE `tid` = #{id}")
    void deleteChild(String id);

    @Select("SELECT * FROM `comment` WHERE `id` = #{id}")
    Comment getOneById(String id);
}
