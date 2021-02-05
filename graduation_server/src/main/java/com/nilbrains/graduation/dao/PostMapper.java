package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Post;
import com.nilbrains.graduation.response.PostRecommend;
import com.nilbrains.graduation.response.PostWithUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PostMapper {

    @Insert("INSERT INTO `post`(`pid`,`title`,`uid`,`cid`,`content`,`type`,`cover`,`status`,`views`,`publish_time`,`desc`,`tags`) " +
            "VALUES(#{pid},#{title},#{uid},#{cid},#{content},#{type},#{cover},#{status},#{views},#{publishTime},#{desc},#{tags})")
    void save(Post post);

    @Select("SELECT * FROM `post` WHERE `pid` = #{pid}")
    @Results(id = "postWithUser", value = {
            @Result(column = "uid", property = "user",
                    one = @One(select = "com.nilbrains.graduation.dao.UserMapper.getUserByUid")),
            @Result(column = "cid", property = "category",
                    one = @One(select = "com.nilbrains.graduation.dao.CategoryMapper.findOneByCid")
            )
    })
    PostWithUser getOne(String pid);

    @Update("UPDATE `post` SET `title`=#{title},`cid`=#{cid},`content`=#{content},`cover`=#{cover},`desc`=#{desc},`tags`=#{tags},`answer`=#{answer} WHERE `pid`=#{pid}")
    void updatePost(Post post);

    @Update("UPDATE `post` SET `views`=`views`+1  WHERE `pid`=#{pid}")
    void addViews(String pid);

    @Select("SELECT * FROM `post` WHERE `pid` = #{pid}")
    Post getSimpleOne(String pid);

    @Update("UPDATE `post` SET `status`=#{status}  WHERE `pid`=#{pid}")
    void delete(String pid, String status);

    @Select("<script>SELECT count(*) FROM `post` " +
            "<where> <if test='title != null and title != &quot;&quot;'>" +
            " and `title` like concat('%',#{title},'%') " +
            "</if> <if test='cid != null and cid != &quot;&quot;'>" +
            " and `cid` = #{cid} " +
            "</if> <if test='type != null and type != &quot;&quot;'>" +
            " and `type` = #{type} " +
            "</if> <if test='answer != null and answer != &quot;&quot;'>" +
            " and `answer` = #{answer} " +
            "</if> <if test='uid != null and uid != &quot;&quot;'>" +
            " and `uid` = #{uid} " +
            "</if> <if test='status != null and status != &quot;&quot;'>" +
            " and `status` = #{status} " +
            "</if> </where> </script>")
    int count(String title, String cid, String type, String uid, String answer, String status);


    @Select("<script>SELECT `pid`,`title`,`uid`,`cid`,`type`,`cover`,`desc`,`answer`,`status`,`views`,`publish_time` FROM `post` " +
            "<where> <if test='title != null and title != &quot;&quot;'>" +
            " and `title` like concat('%',#{title},'%') " +
            "</if> <if test='cid != null and cid != &quot;&quot;'>" +
            " and `cid` = #{cid} " +
            "</if> <if test='type != null and type != &quot;&quot;'>" +
            " and `type` = #{type} " +
            "</if> <if test='uid != null and uid != &quot;&quot;'>" +
            " and `uid` = #{uid} " +
            "</if> <if test='answer != null and answer != &quot;&quot;'>" +
            " and `answer` = #{answer} " +
            "</if> <if test='status != null and status != &quot;&quot;'>" +
            " and `status` = #{status} " +
            "</if> </where>" +
            " ORDER BY `publish_time` DESC LIMIT #{limit},#{offset} </script>")
    @ResultMap("postWithUser")
    List<PostWithUser> list(String title, String cid, String type, String uid, String answer, String status, int limit, int offset);

    @Update("UPDATE `post` SET `answer`=#{answer}  WHERE `pid`=#{pid}")
    void changeAnswer(String pid, String answer);

    @Select("SELECT `tags` FROM `post` WHERE `pid` = #{pid}")
    String getTagsByPid(String pid);

    @Select("SELECT `pid`,`title`,`uid`,`cid`,`type`,`cover`,`desc`,`answer`,`status`,`views`,`publish_time`,`tags`" +
            " FROM `post` WHERE `tags` LIKE #{tags} ORDER BY `publish_time` DESC LIMIT #{size}")
    List<PostRecommend> listPostByLikeTag(String tags, int size);

    @Select("SELECT `pid`,`title`,`uid`,`cid`,`type`,`cover`,`desc`,`answer`,`status`,`views`,`publish_time`,`tags`" +
            " FROM `post` ORDER BY `publish_time` DESC LIMIT #{dxSize}")
    List<PostRecommend> listLastedPostBySize(int dxSize);

    @Select("<script>SELECT count(*) FROM `post` " +
            "<where> <if test='type != null and type != &quot;&quot;'>" +
            " and `type` = #{type} " +
            "</if> </where> </script>")
    int getCount(String type);
}
