package com.nilbrains.graduation.utils;

import com.nilbrains.graduation.pojo.User;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

/**
 * JSON WEB TOKEN
 * 荷载信息
 * 创建 以及 解析
 */
public class ClaimsUtils {

    public static final String ID = "uid";
    public static final String ACCOUNT = "account";
    public static final String NAME = "name";
    public static final String ROLE = "role";
    public static final String AVATAR = "avatar";
    public static final String SIGN = "sign";

    
    public static Map<String, Object> user2Claims(User zeroUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, zeroUser.getUid());
        claims.put(NAME, zeroUser.getName());
        claims.put(ACCOUNT, zeroUser.getAccount());
        claims.put(ROLE, zeroUser.getRole());
        claims.put(AVATAR, zeroUser.getAvatar());
        claims.put(SIGN, zeroUser.getSign());
        return claims;
    }

    public static User claims2User(Claims claims) {
        User zeroUser = new User();
        String id = (String) claims.get(ID);
        zeroUser.setUid(id);
        String account = (String) claims.get(ACCOUNT);
        zeroUser.setAccount(account);
        String name = (String) claims.get(NAME);
        zeroUser.setName(name);
        String role = (String) claims.get(ROLE);
        zeroUser.setRole(role);
        String avatar = (String) claims.get(AVATAR);
        zeroUser.setAvatar(avatar);
        String sign = (String) claims.get(SIGN);
        zeroUser.setSign(sign);
        return zeroUser;
    }
}
