package com.reservoir.controller;

import com.reservoir.core.entity.RoleEnum;
import jakarta.annotation.Resource;
import io.swagger.v3.oas.annotations.Operation;
import com.reservoir.core.entity.Result;
import java.time.LocalDateTime;

import com.reservoir.entity.admin.SystemUser;
import com.reservoir.service.SystemUserService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/systemuser")
@Tag(name = "SystemUser API")
public class SystemUserController {

    @Resource
    private SystemUserService systemuserservice;

    @GetMapping(value = "getAll")
    @Operation(summary = "获取所有SystemUser", description = "获取所有SystemUser记录")
    public Result<?>  getAll() {
        return systemuserservice.getAll();
    }

    @GetMapping(value = "/getById/{id}")
    @Operation(summary = "根据ID获取SystemUser", description = "根据ID获取SystemUser记录")
    public Result<?> getById(@PathVariable Long id) {
        return systemuserservice.getById(id);
    }

    @GetMapping(value = "/getByUsername")
    @Operation(summary = "根据username获取SystemUser", description = "根据username获取SystemUser记录")
    public Result<?> getByUsername(@RequestParam String username) {
        return systemuserservice.getByUsername(username);
    }

    @GetMapping(value = "/getByPassword")
    @Operation(summary = "根据password获取SystemUser", description = "根据password获取SystemUser记录")
    public Result<?> getByPassword(@RequestParam String password) {
        return systemuserservice.getByPassword(password);
    }

    @GetMapping(value = "/getByRole")
    @Operation(summary = "根据role获取SystemUser", description = "根据role获取SystemUser记录")
    public Result<?> getByRole(@RequestParam RoleEnum role) {
        return systemuserservice.getByRole(role);
    }

    @GetMapping(value = "/getByLastLoginTime")
    @Operation(summary = "根据lastLoginTime获取SystemUser", description = "根据lastLoginTime获取SystemUser记录")
    public Result<?> getByLastLoginTime(@RequestParam LocalDateTime lastLoginTime) {
        return systemuserservice.getByLastLoginTime(lastLoginTime);
    }

    @GetMapping(value = "/getByCreatedAt")
    @Operation(summary = "根据createdAt获取SystemUser", description = "根据createdAt获取SystemUser记录")
    public Result<?> getByCreatedAt(@RequestParam LocalDateTime createdAt) {
        return systemuserservice.getByCreatedAt(createdAt);
    }

    @GetMapping(value = "/getByUpdatedAt")
    @Operation(summary = "根据updatedAt获取SystemUser", description = "根据updatedAt获取SystemUser记录")
    public Result<?> getByUpdatedAt(@RequestParam LocalDateTime updatedAt) {
        return systemuserservice.getByUpdatedAt(updatedAt);
    }

    @GetMapping(value = "/getByDeletedAt")
    @Operation(summary = "根据deletedAt获取SystemUser", description = "根据deletedAt获取SystemUser记录")
    public Result<?> getByDeletedAt(@RequestParam LocalDateTime deletedAt) {
        return systemuserservice.getByDeletedAt(deletedAt);
    }

    @GetMapping(value = "/getByAvatarUrl")
    @Operation(summary = "根据avatarUrl获取SystemUser", description = "根据avatarUrl获取SystemUser记录")
    public Result<?> getByAvatarUrl(@RequestParam String avatarUrl) {
        return systemuserservice.getByAvatarUrl(avatarUrl);
    }

    @GetMapping(value = "/page")
    @Operation(summary = "分页查询SystemUser", description = "分页查询SystemUser记录")
    public Result<?> page(@RequestParam int pageNum, @RequestParam int pageSize) {
        return systemuserservice.page(pageNum, pageSize);
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存SystemUser", description = "保存SystemUser记录")
    public Result<?> save(@RequestBody SystemUser systemuser) {
        return systemuserservice.save(systemuser);
    }

    @PutMapping(value = "/update")
    @Operation(summary = "更新SystemUser", description = "更新SystemUser记录")
    public Result<?> update(@RequestBody SystemUser systemuser) {
        return systemuserservice.update(systemuser);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "删除SystemUser", description = "根据ID删除SystemUser记录")
    public Result<?> delete(@PathVariable Long id) {
        return systemuserservice.delete(id);
    }
}
