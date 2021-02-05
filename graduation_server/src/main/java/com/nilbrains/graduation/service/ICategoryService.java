package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.Category;
import com.nilbrains.graduation.response.ResponseResult;

public interface ICategoryService {
    ResponseResult add(Category category);

    ResponseResult modify(Category category);

    ResponseResult changeStatus(String cid);

    ResponseResult list(String status);
}
