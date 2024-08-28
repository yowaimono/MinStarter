package com.reservoir.core.hanlder;

import com.reservoir.core.annotation.Auth;
import com.reservoir.core.entity.RoleEnum;
import com.reservoir.core.exception.AuthException;
import com.reservoir.core.utils.AESUtil;
import com.reservoir.core.utils.Min;
import com.reservoir.core.utils.MinMap;
import com.reservoir.core.utils.StringUtils;
import com.reservoir.entity.user.User;
import com.reservoir.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class AuthAspect {

    @Resource
    AESUtil aesUtil;

    @Resource
    UserMapper userMapper;

    @Before("@within(com.reservoir.core.annotation.Auth) || @annotation(com.reservoir.core.annotation.Auth)")
    public void authenticate(JoinPoint joinPoint) throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        log.info("auth_token: " + token);





        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Auth methodAuth = method.getAnnotation(Auth.class);



//        if (isGuest) {
//            log.info("access guest api ...");
//            return;
//        }

        // 获取类上的注解
        Auth classAuth = joinPoint.getTarget().getClass().getAnnotation(Auth.class);

        // 合并角色列表
        RoleEnum[] allowedRoles = methodAuth != null ? methodAuth.roles() : classAuth.roles();

        // 如果游客可以访问
        Boolean isGuest = Arrays.asList(allowedRoles).contains(RoleEnum.GUEST);

        if (isGuest) {
            log.info("access guest api ...");
            return;
        }


        if (token == null || !token.startsWith("Bearer ")) {
            throw new AuthException("Invalid token or token is null or empty");
        }

        token = token.substring(7); // 去掉 "Bearer " 前缀

        log.info("auth_token: " + token);

        // 验证 token
        String id = MinMap.get(token);
        log.info("id ---------> {}",id);

        if (id == null) {
            throw new AuthException("Invalid token or user is not login!");
        }

        // 缓存里面拿token
        String mtoken =MinMap.get(id);
        log.info("mtoken ------>{}",mtoken);

        // 如果拿出来不是同一个

        if (mtoken != null && !mtoken.equals(token)) {
            throw new AuthException("Invalid token!");
        }

        int user_id = Integer.parseInt(id); //获取id



        // 解密

        String oToken = aesUtil.decrypt(token);
        log.info("oToken: {}", oToken);
        String[] parts = StringUtils.split(oToken,":");
        log.info("auth_token: {}", oToken);
        log.info("parts: {}", Arrays.toString(parts));
        // 0 前缀 1 token 2 id
        if (parts.length != 3) {
            throw new AuthException("Invalid token or token is not true token!");
        }

        if (Integer.parseInt(parts[2]) != user_id) {
            throw new AuthException("Invalid token");
        }

        String res =MinMap.get("ban: "+id);



        if (res != null && res.equals("true")) {
            log.info("user id baned!");
           throw  new AuthException("user is baned!");
        }

        // 从数据库查询用户角色
        User u = Min.select("role").where("id",user_id).first(userMapper);



        if (u == null || u.getRole() == null) {
            throw new AuthException("User role not found");
        }


        // 检查用户角色是否在允许的角色列表中
        RoleEnum userRole = u.getRole();
        boolean isAllowed = Arrays.asList(allowedRoles).contains(userRole);

        if (!isAllowed) {
            throw new AuthException("Permission denied");
        }

    }
}