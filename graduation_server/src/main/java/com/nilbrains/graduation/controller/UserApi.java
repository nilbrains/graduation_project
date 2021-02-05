package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.service.IUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserApi {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserinfoService userinfoService;

    @PreAuthorize("@permission.login()")
    @GetMapping("/user/userinfo")
    public ResponseResult getUserInfoByUid() {
        return userinfoService.getUserInfoByUid();
    }

    @PreAuthorize("@permission.login()")
    @PostMapping("/user/signIn")
    public ResponseResult userSignIn() {
        return userinfoService.userSignIn();
    }

    // 初始化 管理员账号
    @PostMapping("/user/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user, HttpServletRequest request) {
        return userService.initManagerAccount(user, request);
    }

    // 注册用户
    @PostMapping("/user")
    public ResponseResult register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    //  登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        return userService.login(user);
    }

    //  获取用户信息(用户token)
    @GetMapping("/user/info")
    public ResponseResult info() {
        return userService.getUserInfoByToken();
    }

    //  获取用户信息(用户id)
    @GetMapping("/user/{uid}")
    public ResponseResult info(@PathVariable("uid") String uid) {
        return userService.getUserInfoByUid(uid);
    }


    //  修改用户信息
    @PreAuthorize("@permission.login()")
    @PutMapping("/user")
    public ResponseResult modifyUser(
            @RequestBody User user
    ) {
        return userService.modifyUser(user);
    }

    // 修改密码
    @PreAuthorize("@permission.login()")
    @PutMapping("/user/pass")
    public ResponseResult modifyPass(@RequestBody User user) {
        return userService.modifyPass(user);
    }

    // 获取用户列表 需要管理员权限
    @PreAuthorize("@permission.admin()")
    @GetMapping("/user/list")
    public ResponseResult userList(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "current_page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        return userService.userList(name, currentPage, pageSize);
    }

    @PreAuthorize("@permission.admin()")
    @PostMapping("/user/{uid}/status")
    public ResponseResult userStatus(
            @PathVariable("uid") String uid
    ) {
        return userService.changeUserStatus(uid);
    }

    @PreAuthorize("@permission.login()")
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return ResponseResult.SUCCESS("退出成功");
    }
}
