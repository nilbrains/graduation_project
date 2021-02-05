package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.pojo.Category;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ICategoryService;
import com.nilbrains.graduation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryApi {

    @Autowired
    private ICategoryService categoryService;

    //  获取分类列表
    @GetMapping("/cate/list")
    public ResponseResult list(
            @RequestParam(name = "status", defaultValue = Constants.Status.NULL) String status
    ) {
        ResponseResult responseResult = null;
        if (Constants.Status.NULL.equals(status) || Constants.Status.ARTICLE.equals(status)) {
            responseResult = categoryService.list(Constants.Status.ACTIVE);
        } else if (Constants.Status.INACTIVE.equals(status)) {
            responseResult = categoryService.list(Constants.Status.NULL);
        }
        return responseResult;
    }

    @GetMapping("/cate/list/admin")
    public ResponseResult listAdmin() {
        return categoryService.list(Constants.Status.NULL);
    }

    @PreAuthorize("@permission.admin()")
    @PostMapping("/cate")
    // 添加分类
    public ResponseResult add(@RequestBody Category category) {
        return categoryService.add(category);
    }

    //  编辑分类
    @PreAuthorize("@permission.admin()")
    @PutMapping("/cate")
    public ResponseResult modify(@RequestBody Category category) {
        return categoryService.modify(category);
    }

    //  删除分类
    @PreAuthorize("@permission.admin()")
    @PostMapping("/cate/{cid}/status")
    public ResponseResult delete(
            @PathVariable(name = "cid") String cid
    ) {
        return categoryService.changeStatus(cid);
    }
}
