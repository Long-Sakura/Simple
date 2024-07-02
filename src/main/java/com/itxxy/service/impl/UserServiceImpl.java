package com.itxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itxxy.pojo.User;
import com.itxxy.pojo.common.ErrorCode;
import com.itxxy.service.UserService;
import com.itxxy.mapper.UserMapper;
import com.itxxy.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.itxxy.constant.UserConstant.SALT;
import static com.itxxy.constant.UserConstant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String account, String password, String checkPassword, String identityCode) {
        /**
         * 不能为空
         */
        if (StringUtils.isAnyBlank(account, password, checkPassword,identityCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不能为空");
        }
        /**
         * 账号必须大于等于4位
         */
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号必须大于等于4位");
        }
        /**
         * 密码必须大于8位
         */
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码必须大于8位");
        }
        /**
         * 不能包含特殊字符
         */
        String validPattern = "[^a-zA-Z0-9\\s]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不能包含特殊字符");
        }
        /**
         * 第二次输入必须正确
         */
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"第二次输入必须正确");
        }
        /**
         * 判断账号不能重复
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"判断账号不能重复");
        }
        /**
         * 身份码不能重复
         */
         queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identity_code", identityCode);
        long count1 = userMapper.selectCount(queryWrapper);
        if (count1 > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"身份码不能重复");
        }
        /**
         * 对密码进行加密
         */
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + account).getBytes());
        /**
         * 进行存储
         */
        User user = new User();
        user.setAccount(account);
        user.setPassword(encryptPassword);
        user.setIdentityCode(identityCode);
        /**
         * 验证存储是否成功,防止自动拆箱错误
         */
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"验证存储是否成功,防止自动拆箱错误");
        }
        /**
         * 返回ID
         */
        return user.getId();
    }

    @Override
    public User userLogin(String account, String password, HttpServletRequest request) {
        /**
         * 不能为空
         */
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码不能为空");
        }
        /**
         * 账号必须大于等于4位
         */
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号必须大于等于4位");
        }
        /**
         * 密码必须大于8位
         */
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码必须大于8位");
        }
        /**
         * 加密
         */
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + account).getBytes());
        //查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed,account cannot match password");
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
        }
        //用户信息脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
//        return ;
    }

    /**
     * 用户信息脱敏
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User user) {
        if(user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAccount(user.getAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setStatus(0);
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(new Date());
        safetyUser.setIdentityCode(user.getIdentityCode());
        return safetyUser;
    }

    /**
     * 退出登录
     */
    @Override
    public String userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return "退出登录";
    }
}




