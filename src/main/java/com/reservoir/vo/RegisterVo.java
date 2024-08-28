package com.reservoir.vo;

import com.reservoir.core.annotation.Desensitization;
import com.reservoir.core.entity.DesensitizationTypeEnum;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;




@Data
public class RegisterVo {
    @Email(message = "邮箱格式不正确！")
    private String email;

    @Size(min = 8, max = 20, message = "密码长度必须在8到20个字符之间！")
    @Pattern(regexp = "^\\S*$", message = "密码不能有空格！")
    private String password;

    @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间！")
    @Pattern(regexp = "^\\S*$", message = "用户名不能有空格！")
    private String username;
}
