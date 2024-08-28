package com.reservoir.controller.user;


import com.reservoir.core.annotation.Auth;
import com.reservoir.core.annotation.RateLimit;
import com.reservoir.core.entity.Result;
import com.reservoir.core.entity.ResultCode;
import com.reservoir.core.entity.RoleEnum;

import com.reservoir.entity.user.User;
import com.reservoir.service.UserService;
import com.reservoir.vo.LoginVo;
import com.reservoir.vo.RegisterVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
@Auth(roles = {RoleEnum.USER,RoleEnum.ADMIN})
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/del/{id}")
    public Result<?> del(@PathVariable Long id) {
        return userService.logicDel(id);
    }


    @PutMapping("/update")
    @Operation(summary = "更新用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> delete(@RequestBody User user) {
        return userService.delete(user);
    }

    @GetMapping("/findById/{id}")
    @Operation(summary = "根据ID查找用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> findById(@PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping("/findAll")
    @Operation(summary = "查找所有用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> findAll() {
        return userService.findAll();
    }

    @GetMapping("/findByEmail")
    @Operation(summary = "根据邮箱查找用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> findByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }



    @GetMapping("/findByUsername")
    @Operation(summary = "根据用户名查找用户", description = "测试")
    @RateLimit(value = 5)
    public Result<?> findByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }


    @PostMapping("/add")
    @Operation(summary = "添加用户",description = "测试")
    @RateLimit(value = 5)
    public Result<?> add(User user) {
        return userService.add(user);
    }

    @PostMapping("/register")
    @Operation(summary = "注册用户",description = "注册测试")
    @RateLimit(value = 5)
    @Auth(roles = RoleEnum.GUEST)
    public Result<?> register(@Valid @RequestBody RegisterVo user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    @Operation(summary = "登录用户",description = "登录测试")
    @RateLimit(value = 5)
    @Auth(roles = RoleEnum.GUEST)
    public Result<?> login(@Valid @RequestBody LoginVo u) throws Exception {
        return userService.login(u);
    }

    @GetMapping("/auth")
    @Operation(summary = "测试")
    @Auth(roles = {RoleEnum.ADMIN,RoleEnum.USER})
    public Result<?> auth(@RequestHeader(value = "Authorization",required = false) String token) {
        return Result.of(ResultCode.SUCCESS);
    }

    @GetMapping("/ban/{id}")
    @Operation(summary = "封禁用户测试")
    public Result<?> ban(@PathVariable("id") Integer userId) {
        return userService.ban(userId);
    }

    @PostMapping("/upPw")
    @Operation(summary = "修改密码测试")
    public Result<?> upPw(@RequestHeader("Authorization") String token,String newPw) throws Exception {
        return userService.updatePw(token,newPw);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<?> logout(@RequestHeader("Authorization") String token) throws Exception {
        return userService.logout(token);
    }

}
