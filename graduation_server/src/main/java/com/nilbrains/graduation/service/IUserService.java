package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    /**
     * 初始化 管理员
     *
     * @param user    用户对象
     * @param request XXX
     * @return ResponseResult
     */
    ResponseResult initManagerAccount(User user, HttpServletRequest request);

    /**
     * 注册 用户
     *
     * @param user 用户
     * @return ResponseResult
     */
    ResponseResult registerUser(User user);

    /**
     * 用户 登录
     *
     * @param user 用户
     * @return ResponseResult
     */
    ResponseResult login(User user);

    /**
     * 获取 已登录的 用户的信息
     * 由于是 http header 里的 jwt 解析得来
     * 所以用户信息会有一定的延时
     * 但用户的 id 不会改变
     * 需要最新的用户信息的话去数据库里查询最新的数据
     *
     * @return 用户信息
     */
    User checkUser();

    /**
     * 获取 用户信息 并返回
     *
     * @return ResponseResult
     */
    ResponseResult getUserInfoByToken();

    /**
     * 通过 用户Id 查询 用户信息
     *
     * @param uid 用户Id
     * @return ResponseResult
     */
    ResponseResult getUserInfoByUid(String uid);

    /**
     * 修改用户信息
     *
     * @param user User
     * @return ResponseResult
     */
    ResponseResult modifyUser(User user);

    /**
     * 修改用户密码
     *
     * @param user User
     * @return ResponseResult
     */
    ResponseResult modifyPass(User user);

    /**
     * 获取用户列表
     *
     * @param name        用户昵称
     * @param currentPage 当前页
     * @param pageSize    size
     * @return ResponseResult
     */
    ResponseResult userList(String name, int currentPage, int pageSize);

    /**
     * 改变用户的禁用状态
     *
     * @param uid 用户id
     * @return ResponseResult
     */
    ResponseResult changeUserStatus(String uid);
}
