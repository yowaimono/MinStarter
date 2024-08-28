package com.reservoir.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.reservoir.core.annotation.Desensitization;
import com.reservoir.core.entity.DesensitizationTypeEnum;
import com.reservoir.core.entity.RoleEnum;
import com.reservoir.entity.base.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("c_user")
@Accessors(chain = true)
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @TableField(value = "username", condition = SqlCondition.EQUAL)
    private String username;

    @Desensitization(type = DesensitizationTypeEnum.PASSWORD)
    @TableField(value = "password")
    private String password;

    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    @TableField(value = "email")
    @Email
    private String email;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "deleted_at")
    @TableLogic(value = "NULL", delval = "NOW()")
    private LocalDateTime deletedAt;

    @Schema(description = "角色", example = "ADMIN")
    @TableField(value = "role")
    private RoleEnum role;
}