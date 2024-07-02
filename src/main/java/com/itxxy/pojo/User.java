package com.itxxy.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String account;

    /**
     * 
     */
    private String avatarUrl;

    /**
     * 0--男
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户状态,0--正常
     */
//    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 数据是否可用 0--可用
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 身份类别
     */
    private Integer userRole;

    private String identityCode;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户表
     * @TableName user
     */

}