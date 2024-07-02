package com.itxxy.pojo.request;

import lombok.Data;

@Data
public class UserLogin {
    String account;
    String password;
    String checkPassword;
    String identityCode;
}
