package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.SettingMapper;
import com.nilbrains.graduation.dao.UserMapper;
import com.nilbrains.graduation.pojo.Setting;
import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.pojo.Userinfo;
import com.nilbrains.graduation.response.ResponsePage;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.service.IUserinfoService;
import com.nilbrains.graduation.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SettingMapper settingMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public ResponseResult initManagerAccount(User user, HttpServletRequest request) {

        //判断是否 已经初始化
        Setting manageAccountState = settingMapper.querySettingByKey(Constants.Setting.MANAGE_ACCOUNT_INIT_STATE);

        if (manageAccountState != null) {
            return ResponseResult.FAILED("管理员账号已经初始化..");
        }

        if (TextUtils.isEmpty(user.getAccount())) {
            return ResponseResult.FAILED("账号不能为空");
        }

        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
//        补全
        user.setUid(String.valueOf(idWorker.nextId()));
        user.setName(user.getAccount());
        user.setRole(Constants.User.ROLE_ADMIN_USER);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setSign(Constants.User.DEFAULT_SIGN);
        user.setStatus(Constants.Status.ACTIVE);
//      获取密码
        String password = user.getPassword();
//        密码加密
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.saveUser(user);
//        初始化
        userinfoService.initUserInfo(user.getUid());
        Setting setting = new Setting();
        setting.setKey(Constants.Setting.MANAGE_ACCOUNT_INIT_STATE);
        setting.setValue("1");
        settingMapper.saveSetting(setting);
        return ResponseResult.SUCCESS("初始化成功");
    }

    @Override
    public ResponseResult registerUser(User user) {

        if (TextUtils.isEmpty(user.getAccount())) {
            return ResponseResult.FAILED("账号不能为空");
        }

        User userByAccount = userMapper.findUserByAccount(user.getAccount());

        if (userByAccount != null) {
            return ResponseResult.FAILED("该账号已注册");
        }

        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }

        //        补全
        user.setUid(String.valueOf(idWorker.nextId()));
        user.setName(user.getAccount());
        user.setRole(Constants.User.ROLE_USER);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setSign(Constants.User.DEFAULT_SIGN);
        user.setStatus(Constants.Status.ACTIVE);
//      获取密码
        String password = user.getPassword();
//        密码加密
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.saveUser(user);
        userinfoService.initUserInfo(user.getUid());
        return ResponseResult.SUCCESS("创建成功");
    }

    @Override
    public ResponseResult login(User user) {

        String account = user.getAccount();
        if (TextUtils.isEmpty(account)) {
            return ResponseResult.FAILED("账号不能为空");
        }
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空.");
        }

        User userFromDb = userMapper.findUserByAccount(account);

        if (userFromDb == null) {
            return ResponseResult.FAILED("账号或密码不正确");
        }
//        对比密码
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("账号或密码不正确");
        }

        if (Constants.Status.INACTIVE.equals(userFromDb.getStatus())) {
            return ResponseResult.FAILED("该账号已被禁用");
        }

//        jwt生成
//        JwtUtils.createToken()

        Map<String, Object> claims = ClaimsUtils.user2Claims(userFromDb);

        String token = JwtUtils.createToken(claims);

        return ResponseResult.LOGIN_SUCCESS().setData(token);
    }

    @Override
    public User checkUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(Constants.Setting.HEADER_AUTH);
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        try {
            Claims claims = JwtUtils.parseJWT(token);
            return ClaimsUtils.claims2User(claims);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseResult getUserInfoByToken() {
        User userByCheck = this.checkUser();
        User user = userMapper.getUserByUid(userByCheck.getUid());
        return ResponseResult.SUCCESS().setData(user);
    }

    @Override
    public ResponseResult getUserInfoByUid(String uid) {
        if (TextUtils.isEmpty(uid)) {
            return ResponseResult.FAILED("用户ID不能为空");
        }

        User user = userMapper.getUserByUid(uid);

        if (user == null) {
            return ResponseResult.FAILED("木有此用户");
        }
//        去密码
        user.setPassword(null);
        Userinfo userinfo = userinfoService.getUserinfo(uid);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", user);
        userMap.put("userinfo", userinfo);
        return ResponseResult.SUCCESS("获取用户信息成功").setData(userMap);
    }

    @Override
    public ResponseResult modifyUser(User user) {
//        获取 当前操作用户
        User userCheck = this.checkUser();
        if (TextUtils.isEmpty(user.getUid())) {
            return ResponseResult.FAILED("用户名ID不能为空");
        }
//        是不是普通用户 是
        if (Constants.User.ROLE_USER.equals(userCheck.getRole()) && !userCheck.getUid().equals(user.getUid())) {
            return ResponseResult.ERROR_403();
        }
        if (TextUtils.isEmpty(user.getName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getSign())) {
            return ResponseResult.FAILED("签名不能为空");
        }
        if (TextUtils.isEmpty(user.getAvatar())) {
            return ResponseResult.FAILED("头像不能为空");
        }

        userMapper.updateUser(user);

        return ResponseResult.SUCCESS("修改成功");
    }

    @Override
    public ResponseResult modifyPass(User user) {
        User userCheck = this.checkUser();
        if (TextUtils.isEmpty(user.getUid())) {
            return ResponseResult.FAILED("用户名ID不能为空");
        }
        if (Constants.User.ROLE_USER.equals(userCheck.getRole()) && !userCheck.getUid().equals(user.getUid())) {
            return ResponseResult.ERROR_403();
        }
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不能为空");
        }
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        userMapper.updatePass(user);
        return ResponseResult.SUCCESS("密码修改成功");
    }

    @Override
    public ResponseResult userList(String name, int currentPage, int pageSize) {
        int totalCount = userMapper.getCount(name);
        ResponsePage responsePage = new ResponsePage();
        responsePage.setTotalCount(totalCount);
        responsePage.setCurrentPage(currentPage);
        responsePage.setPageSize(pageSize);
        responsePage.setCountPage((int) Math.ceil(1.0 * totalCount / pageSize));
        if (totalCount == 0) {
            return ResponseResult.SUCCESS("没有数据了").setData(responsePage);
        }
        List<User> users = userMapper.getUsers(name, pageSize * (currentPage - 1), pageSize);
        responsePage.setPageData(users);
        return ResponseResult.SUCCESS("获取成功").setData(responsePage);
    }

    @Override
    public ResponseResult changeUserStatus(String uid) {
        userMapper.updateStatus(uid);
        return ResponseResult.SUCCESS("切换成功");
    }
}
