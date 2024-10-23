package com.reservoir.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.reservoir.core.annotation.Desensitization;
import com.reservoir.core.entity.DesensitizationTypeEnum;
import com.reservoir.core.entity.RoleEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@Accessors(chain = true)
@TableName("system_user")
public class SystemUser {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;

    @Desensitization(type = DesensitizationTypeEnum.PASSWORD)
    private String password;

    private RoleEnum role;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String avatarUrl = "momo";

}