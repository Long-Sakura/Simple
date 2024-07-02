package com.itxxy.service;

import com.itxxy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {
    /**
     * @param account      用户账号
     * @param password     密码
     * @param checkWord    二次密码
     * @param identityCode
     * @return 用户插入后的ID
     */
    long userRegister(String account, String password, String checkWord, String identityCode);

    /**
     *
     * @param account 用户账号
     * @param password 密码
     * @return 脱敏后的用户信息
     */

    User userLogin(String account, String password, HttpServletRequest request);

    User getSafetyUser(User user);

    /**
     * 注销
     * @param request
     */
    String userLogout(HttpServletRequest request);
}
