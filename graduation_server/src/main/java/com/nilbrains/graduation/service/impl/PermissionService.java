package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permission")
public class PermissionService {

    @Autowired
    private IUserService userService;

    public boolean login() {
        User user = userService.checkUser();
        return user != null;
    }

    public boolean admin() {
        User user = userService.checkUser();
        if (user == null) {
            return false;
        }
        //            管理员
        return Constants.User.ROLE_ADMIN_USER.equals(user.getRole());
    }

}
