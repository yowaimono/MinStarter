package com.reservoir.core.utils;

import com.reservoir.core.exception.AuthException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class TokenUtils {
    private static final String BEARER_PREFIX = "Bearer ";

    @Resource
    AESUtil aesUtil;

    public int extractUserId(String token) throws Exception {
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new AuthException("Invalid token or token is null or empty");
        }

        token = token.substring(BEARER_PREFIX.length()); // 去掉 "Bearer " 前缀

        log.info("auth_token: " + token);

        // 验证 token
        String id = MinMap.get(token);

        if (id == null) {
            throw new AuthException("Invalid token or user is not login!");
        }

        int user_id = Integer.parseInt(id); // 获取 id

        // 解密
        String oToken = aesUtil.decrypt(token);
        log.info("oToken: {}", oToken);
        String[] parts = StringUtils.split(oToken, ":");
        log.info("auth_token: {}", oToken);
        log.info("parts: {}", Arrays.toString(parts));

        // 0 前缀 1 token 2 id
        if (parts.length != 3) {
            throw new AuthException("Invalid token or token is not true token!");
        }

        if (Integer.parseInt(parts[2]) != user_id) {
            throw new AuthException("Invalid token");
        }

        return user_id;
    }
}
