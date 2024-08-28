package com.reservoir.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.reservoir.core.annotation.Desensitization;
import com.reservoir.core.entity.DesensitizationTypeEnum;
import com.reservoir.core.entity.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data

@Accessors(chain = true)
public class UserInfo {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;



    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    @Email
    private String email;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private LocalDateTime deletedAt;


    private RoleEnum role;

}