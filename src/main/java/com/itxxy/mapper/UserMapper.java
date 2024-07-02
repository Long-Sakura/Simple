package com.itxxy.mapper;

import com.itxxy.pojo.Log;
import com.itxxy.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

/**
* description 针对表【user(用户表)】的数据库操作Mapper
* Entity com.itxxy.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO log(operator, method_name, description) VALUES " +
            "(#{operator},#{methodName},#{description})")
           void logInsert(Log log);

}




