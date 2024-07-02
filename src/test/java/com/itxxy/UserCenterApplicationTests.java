package com.itxxy;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itxxy.mapper.UserMapper;
import com.itxxy.pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserCenterApplicationTests {
    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","0");
        userMapper.selectList(queryWrapper);
    }

}
