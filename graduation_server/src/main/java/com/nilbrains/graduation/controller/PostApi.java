package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.pojo.Post;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IPostService;
import com.nilbrains.graduation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostApi {
    @Autowired
    private IPostService postService;

    //    获取所有文章、问答列表
    @GetMapping("/post/list")
    public ResponseResult list(
            @RequestParam(name = "title", required = false, defaultValue = Constants.Status.NULL) String title,
            @RequestParam(name = "cid", required = false, defaultValue = Constants.Status.NULL) String cid,
            @RequestParam(name = "type", required = false, defaultValue = Constants.Status.NULL) String type,
            @RequestParam(name = "answer", required = false, defaultValue = Constants.Status.NULL) String answer,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return postService.list(title, cid, type, answer, Constants.Status.ACTIVE, currentPage, pageSize);
    }

    @GetMapping("/post/list/admin")
    public ResponseResult listAdmin(
            @RequestParam(name = "title", required = false, defaultValue = Constants.Status.NULL) String title,
            @RequestParam(name = "cid", required = false, defaultValue = Constants.Status.NULL) String cid,
            @RequestParam(name = "type", required = false, defaultValue = Constants.Status.NULL) String type,
            @RequestParam(name = "answer", required = false, defaultValue = Constants.Status.NULL) String answer,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return postService.list(title, cid, type, answer, Constants.Status.NULL, currentPage, pageSize);
    }

    //    获取所有文章、问答列表
    @PreAuthorize("@permission.login()")
    @GetMapping("/post/user/list")
    public ResponseResult list(
            @RequestParam(name = "type", required = false, defaultValue = Constants.Status.NULL) String type,
            @RequestParam(name = "answer", required = false, defaultValue = Constants.Status.NULL) String answer,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return postService.list(type, answer, Constants.Status.NULL, currentPage, pageSize);
    }

    @GetMapping("/post/user/{uid}/list")
    public ResponseResult list(
            @PathVariable("uid") String uid,
            @RequestParam(name = "type", required = false, defaultValue = Constants.Status.NULL) String type,
            @RequestParam(name = "answer", required = false, defaultValue = Constants.Status.NULL) String answer,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return postService.listByUser(uid, type, answer, Constants.Status.NULL, currentPage, pageSize);
    }

    //   添加文章、问题
    @PreAuthorize("@permission.login()")
    @PostMapping("/post")
    public ResponseResult add(
            @RequestParam(name = "type", required = false, defaultValue = Constants.Status.ACTIVE) String type,
            @RequestBody Post post
    ) {
        return postService.addPost(type, post);
    }

    //  获取文章详情
    @GetMapping("/post/{pid}")
    public ResponseResult simpleInfo(@PathVariable("pid") String pid) {
        return postService.simpleInfo(pid);
    }

    //  获取文章详情
    @GetMapping("/post/{pid}/full")
    public ResponseResult info(@PathVariable("pid") String pid) {
        return postService.info(pid);
    }

    //    修改文章
    @PreAuthorize("@permission.login()")
    @PutMapping("/post")
    public ResponseResult modify(@RequestBody Post post) {
        return postService.modify(post);
    }

    //    删除文章
    @PreAuthorize("@permission.login()")
    @DeleteMapping("/post/{pid}")
    public ResponseResult delete(@PathVariable("pid") String pid) {
        return postService.delete(pid);
    }


    //   文章 解决
    @PreAuthorize("@permission.login()")
    @PostMapping("/post/{pid}/answer")
    public ResponseResult changeAnswer(@PathVariable("pid") String pid) {
        return postService.changeAnswer(pid);
    }


    @GetMapping("/post/recommend")
    public ResponseResult listRecommendPost(
            @RequestParam(name = "pid", required = false, defaultValue = Constants.Status.NULL) String pid,
            @RequestParam(name = "size", required = false, defaultValue = Constants.Status.NUM_5) int size
    ) {
        return postService.listRecommendPost(pid, size);
    }

}
