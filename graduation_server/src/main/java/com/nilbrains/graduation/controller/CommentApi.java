package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.pojo.Comment;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentApi {

    @Autowired
    private ICommentService commentService;


    //    todo 根据文章、问题id 获取评论
    @GetMapping("/comment/{pid}")
    public ResponseResult listByPid(
            @PathVariable("pid") String pid,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return commentService.list(pid, currentPage, pageSize);
    }


    //    todo 根据获取评论列表
    @GetMapping("/comments")
    public ResponseResult listByPid(
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return commentService.list(currentPage, pageSize);
    }


    //  添加一条评论
    @PreAuthorize("@permission.login()")
    @PostMapping("/comment/{pid}")
    public ResponseResult add(@PathVariable String pid, @RequestBody Comment comment) {
        return commentService.add(pid, comment);
    }

    //  设置精选
    @PreAuthorize("@permission.login()")
    @PostMapping("/comment/{id}/true")
    public ResponseResult setTrue(@PathVariable String id) {
        return commentService.setTrue(id);
    }

    //  删除评论
    @PreAuthorize("@permission.login()")
    @DeleteMapping("/comment/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return commentService.delete(id);
    }
}
