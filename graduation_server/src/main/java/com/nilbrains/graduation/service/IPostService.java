package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.Post;
import com.nilbrains.graduation.response.ResponseResult;

public interface IPostService {
    ResponseResult addPost(String type, Post post);

    ResponseResult simpleInfo(String pid);

    ResponseResult modify(Post post);

    ResponseResult info(String pid);

    ResponseResult delete(String pid);

    ResponseResult list(String title, String cid, String type, String answer, String status, int currentPage, int pageSize);

    ResponseResult list(String type, String answer, String status, int currentPage, int pageSize);

    ResponseResult changeAnswer(String pid);

    ResponseResult listRecommendPost(String pid, int size);

    ResponseResult listByUser(String uid, String type, String answer, String status, int currentPage, int pageSize);
}
