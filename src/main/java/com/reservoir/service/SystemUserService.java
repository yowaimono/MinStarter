package com.reservoir.service;

import com.reservoir.core.entity.Result;
import java.time.LocalDateTime;

import com.reservoir.core.entity.RoleEnum;
import com.reservoir.entity.admin.SystemUser;

public interface SystemUserService {

    Result<?> getAll();
    Result<?> getById(Long id);
    Result<?> getByUsername(String username);
    Result<?> getByPassword(String password);
    Result<?> getByRole(RoleEnum role);
    Result<?> getByLastLoginTime(LocalDateTime lastLoginTime);
    Result<?> getByCreatedAt(LocalDateTime createdAt);
    Result<?> getByUpdatedAt(LocalDateTime updatedAt);
    Result<?> getByDeletedAt(LocalDateTime deletedAt);
    Result<?> getByAvatarUrl(String avatarUrl);
    Result<?> page(int pageNum, int pageSize);
    Result<?> save(SystemUser systemuser);
    Result<?> update(SystemUser systemuser);
    Result<?> delete(Long id);
}
