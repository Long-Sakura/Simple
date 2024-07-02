package com.itxxy.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itxxy.annotation.Log;
import com.itxxy.pojo.User;
import com.itxxy.pojo.common.ErrorCode;
import com.itxxy.pojo.common.Result;
import com.itxxy.pojo.request.UserLogin;
import com.itxxy.service.UserService;
import com.itxxy.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itxxy.constant.UserConstant.ADMIN_ROLE;
import static com.itxxy.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     *
     * @param userLogin 用户信息DTO类
     * @return 用户id
     */
    @PostMapping("/register")
    @Log
    public Result userRegister(@RequestBody UserLogin userLogin) {
        if (userLogin == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long result = userService.userRegister(userLogin.getAccount(), userLogin.getPassword(), userLogin.getCheckPassword(), userLogin.getIdentityCode());
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Log
    public Result userLogin(@RequestBody UserLogin userLogin, HttpServletRequest request) {
        if (userLogin == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userLogin.getAccount(), userLogin.getPassword(), request);
        return Result.success(user);

    }

    /**
     * 注销
     */
    @PostMapping("/logout")
    @Log
    public Result userLogout(HttpServletRequest request) {
        if (request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        String result = userService.userLogout(request);
        return Result.success(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    @Log
    public Result getUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        long id = currentUser.getId();

        User user = userService.getById(id);
        User safetyUser = userService.getSafetyUser(user);
        return Result.success(safetyUser);
    }

    /**
     * 仅管理员可用, 查询所有用户
     */
    @GetMapping("/search")
    @Log
    public Result searchUsers(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        return Result.success(userList);
    }

    /**
     * 仅管理员可用，删除用户
     */
    @PostMapping("/delete")
    @Log
    public Result delete(@RequestBody User user, HttpServletRequest request) {

        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "不是管理员权限");
        }
        if (user.getId() <= 0)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        Boolean result = userService.removeById(user.getId());
        return Result.success(result);
    }


    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return 返回是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userLoginStatus = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userLoginStatus;
        if (user == null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        return user.getUserRole() == ADMIN_ROLE;
    }
}
