package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.Comment;
import com.nilbrains.graduation.response.ResponseResult;

public interface ICommentService {
    ResponseResult add(String pid, Comment comment);

    ResponseResult delete(String id);

    ResponseResult list(String pid, int currentPage, int pageSize);

    ResponseResult setTrue(String id);

    ResponseResult list(int currentPage, int pageSize);
}
