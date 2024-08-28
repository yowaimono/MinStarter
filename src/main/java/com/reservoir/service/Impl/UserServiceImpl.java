package com.reservoir.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.reservoir.core.entity.Result;
import com.reservoir.core.entity.ResultCode;
import com.reservoir.core.utils.*;

import com.reservoir.entity.user.User;
import com.reservoir.mapper.UserMapper;
import com.reservoir.service.UserService;
import com.reservoir.vo.LoginVo;
import com.reservoir.vo.RegisterVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    AESUtil aesUtil;

    @Resource
    TokenUtils tokenUtils;


    private static final String USER_TOKEN = "user_token:";

    @Resource
    private UserMapper userMapper;

    @Override
    public Result<?> add(User user) {
        try {
            userMapper.insert(user);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(500, "Failed to add user: " + e.getMessage());
        }
    }



    @Override
    public Result<?> ban(Integer id) {
        log.info("begin ban user {}", id);
        // user exist?
        User user = Min.select("username","id").where("id",id).first(userMapper);
        if (user == null) {
            return Result.of(ResultCode.USER_NOT_EXISTS);
        }

        // exist!
        MinMap.set("ban: "+id.toString(),"true",60*60*5L);
        return Result.success(user.getUsername());
    }

    @Override
    public Result<?> update(User user) {
        try {
            userMapper.updateById(user);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(500, "Failed to update user: " + e.getMessage());
        }
    }

    @Override
    public Result<?> delete(User user) {
        try {
            userMapper.deleteById(user.getId());
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(500, "Failed to delete user: " + e.getMessage());
        }
    }

    @Override
    public Result<?> findById(long id) {
        try {
            User user = userMapper.selectById(id);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.error(404, "User not found");
            }
        } catch (Exception e) {
            return Result.error(500, "Failed to find user by ID: " + e.getMessage());
        }
    }

    @Override
    public Result<?> findAll() {
        try {
            List<User> users = userMapper.selectList(null);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(500, "Failed to find all users: " + e.getMessage());
        }
    }

    @Override
    public Result<?> findByEmail(String email) {
        try {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.error(404, "User not found by email");
            }
        } catch (Exception e) {
            return Result.error(500, "Failed to find user by email: " + e.getMessage());
        }
    }

    @Override
    public Result<?> findByUsername(String username) {
        try {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.error(404, "User not found by username");
            }
        } catch (Exception e) {
            return Result.error(500, "Failed to find user by username: " + e.getMessage());
        }
    }

    @Override
    public Result<?> register(RegisterVo u) {
        try {
            // 不需要字段验证
            User user = (User) Min.select("username").where("username", u.getUsername()).or("email", u.getEmail()).first(userMapper);

            log.info("userinfo_exist? --> " + user);
            // 用户是否存在
            if (user != null) {
                log.info("user_exist!!!");
                return Result.of(ResultCode.USER_ALREADY_EXISTS);
            }

            user = new User()
                    .setEmail(u.getEmail())
                    .setUsername(u.getUsername())
                    .setPassword(EncryptionUtil.sha256(u.getPassword()))
                    .setCreatedAt(LocalDateTime.now());
            // 不存在
            log.info("insert_userinfo... --> " + user);
            userMapper.insert(user);
            return Result.of(ResultCode.ADD_USER_SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred during registration", e);
            return Result.of(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Result<?> login(LoginVo user) {
        try {
            // 开始登陆逻辑
            log.info("enter to login...");
            // 首先查询用户信息，字段验证过了直接插，需要用户名和密码

            User u = Min.select("id", "username", "password").where("username", user.getUsername()).first(userMapper);

            log.info("user_exist? --> " + u);
            if (u == null) {
                log.info("user_not_exist!!!");
                return Result.of(ResultCode.USER_NOT_EXISTS);
            }

            // 用户存在，验证密码
            log.info("begin....password");

            if (!u.getPassword().equals(EncryptionUtil.sha256(user.getPassword()))) {
                log.info("password_error!!!");
                return Result.of(ResultCode.PASSWORD_MISMATCH);
            }

            // 密码正确，登录
            // 当前用户是否登录
            String oldToken = (String) MinMap.get(u.getId().toString());

            log.info("oldToken: " + oldToken);
            // 已登录？
            if (oldToken != null) {
                log.info("oldToken_已登录 --> " + u.getUsername() + ":" + oldToken);
                return Result.error(401, "用户已登录！");
            }

            // 未登录
            String token = USER_TOKEN + UUID.randomUUID().toString() + ":" + u.getId();
            // 生成token
            log.info("gen token: " + token);

            String sToken = aesUtil.encrypt(token);

            log.info("gen sToken: " + sToken);

            // 保存
            MinMap.set(u.getId().toString(), sToken, 60 * 60 * 2L);
            MinMap.set(sToken, u.getId().toString(), 60 * 60 * 2L);

            return Result.success(200, "login success!", Map.of("username", u.getUsername(), "token", sToken));

        } catch (Exception e) {
            log.error("An error occurred during login", e);
            return Result.of(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Result<?> logicDel(Long id) {
        Min.update().where("id",id).set("deleted_at",LocalDateTime.now()).UpdateRows(userMapper);
        return Result.success("删除成功！");
    }

    @Override
    public Result<?> updatePw(String token, String newPw) {
        try {
            // 提取Id
            Integer user_id = Objects.requireNonNull(tokenUtils.extractUserId(token), "User ID cannot be null");
            log.info("user_id: {}", user_id);

            // 检查用户是否存在
            Optional<User> userOptional = Optional.ofNullable(Min.select("username", "id", "password")
                    .where("id", user_id)
                    .first(userMapper));

            if (userOptional.isEmpty()) {
                log.info("user_not_exist!!!");
                return Result.of(ResultCode.USER_NOT_EXISTS);
            }

            User u = userOptional.get();
            log.info("update_userinfo... --> {}", u);

            if (EncryptionUtil.sha256(newPw).equals(u.getPassword())) {
                return Result.of(ResultCode.PASSWORD_HISTORY_VIOLATION);
            }

            u.setPassword(EncryptionUtil.sha256(newPw));
            u.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(u);

            // 删除token
            MinMap.delete(token.substring(7));
            MinMap.delete(user_id.toString());

            return Result.success(200, "更新成功", Map.of("username", u.getUsername()));
        } catch (Exception e) {
            log.error("Failed to update password for token: {}", token, e);
            return Result.of(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<?> logout(String token) {
        // 验证token是否为空
        if (StringUtils.isEmpty(token)) {
            log.warn("Logout attempt with empty token");
            return Result.of(ResultCode.INVALID_TOKEN);
        }

        try {
            // 提取用户ID
            Integer user_id = tokenUtils.extractUserId(token);
            if (user_id == null) {
                log.warn("Invalid token provided during logout");
                return Result.of(ResultCode.INVALID_TOKEN);
            }

            // 删除用户ID对应的token
            MinMap.delete(user_id.toString());

            // 删除token
            String tokenWithoutBearer = token.substring(7);
            MinMap.delete(tokenWithoutBearer);

            log.info("User with ID {} logged out successfully", user_id);
            return Result.success(ResultCode.TOKEN_REVOKED);

        } catch (Exception e) {
            log.error("An error occurred during logout", e);
            return Result.of(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

}
