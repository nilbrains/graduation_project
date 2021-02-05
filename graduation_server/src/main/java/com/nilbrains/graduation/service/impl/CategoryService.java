package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.CategoryMapper;
import com.nilbrains.graduation.pojo.Category;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ICategoryService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.IdWorker;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult add(Category category) {
        if (TextUtils.isEmpty(category.getTitle())) {
            return ResponseResult.FAILED("分类标题不能为空");
        }
        Category one = categoryMapper.findOne(category.getTitle());
        if (one != null) {
            return ResponseResult.FAILED("该标题已存在");
        }
        category.setStatus(Constants.Status.ACTIVE);
        category.setCid(String.valueOf(idWorker.nextId()));
        categoryMapper.save(category);
        return ResponseResult.SUCCESS("添加成功");
    }

    @Override
    public ResponseResult modify(Category category) {
        if (TextUtils.isEmpty(category.getCid())) {
            return ResponseResult.FAILED("分类ID不能为空");
        }
        if (TextUtils.isEmpty(category.getTitle())) {
            return ResponseResult.FAILED("分类标题不能为空");
        }
        Category one = categoryMapper.findOne(category.getTitle());
        if (one != null && !one.getCid().equals(category.getCid())) {
            return ResponseResult.FAILED("该标题已存在");
        }
        categoryMapper.update(category);
        return ResponseResult.SUCCESS("修改成功");
    }

    @Override
    public ResponseResult changeStatus(String cid) {
        categoryMapper.changeStatus(cid);
        return ResponseResult.SUCCESS("切换成功");
    }

    @Override
    public ResponseResult list(String status) {
        int totalCount = categoryMapper.getCount(status);
        if (totalCount == 0) {
            return ResponseResult.FAILED("没有数据了");
        }
        List<Category> categories = categoryMapper.getCategories(status);
        return ResponseResult.SUCCESS("获取成功").setData(categories);
    }
}
