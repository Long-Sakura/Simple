package com.itxxy.service;

import com.itxxy.pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testUserService() {
        User user = new User();
        user.setUsername("Dogyedp");
        user.setAccount("1312");
        user.setAvatarUrl("1.jpg");
        user.setEmail("qq.com");
        user.setPassword("2222");
        user.setStatus(2);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assert.isTrue(result, "有错误!");
    }

    @Test
    void userRegister() {
        /**
         * 验证为null/空
         */
        String account = "";
        String password = "88888888";
        String checkPassword = "88888888";
        String identityCode="06";
        long result1 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result1);
        /**
         * 验证账号小于4位
         */
        account = "xxy";
        long result2 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result2);
        /**
         * 密码小于8位
         */
        password = "7777777";
        checkPassword = "7777777";
        long result3 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result3);
        /**
         * 校验特殊字符
         */
        account = "x (yy)";
        long result4 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result4);
        /**
         * 两次密码输入不同
         */
        password="88888888";
        checkPassword = "88888887";
        long result5 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result5);
        /**
         * 账户不能重复
         */
        account = "Dogyedp";
        checkPassword = "88888888";
        long result6 = userService.userRegister(account, password, checkPassword, identityCode);
        Assertions.assertEquals(-1, result6);
        /**
         * 测试是否能真正注册成功
         */
        account="xxyo";
        long result7 = userService.userRegister(account, password, checkPassword,identityCode);
        Assertions.assertTrue(result7>0);
    }
}