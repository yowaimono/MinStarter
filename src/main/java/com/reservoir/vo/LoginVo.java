package com.reservoir.vo;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginVo {
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^\\S*$", message = "密码不能有空格！")
    String username;

    @Size(min = 8, max = 20)
    @Pattern(regexp = "^\\S*$", message = "用户名不能有空格！")
    String password;
}
