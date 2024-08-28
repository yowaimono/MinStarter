
package com.reservoir.service;

import com.reservoir.core.entity.Result;
import com.reservoir.entity.user.User;
import com.reservoir.vo.LoginVo;
import com.reservoir.vo.RegisterVo;

public interface UserService {

    Result<?> add(User user);
    Result<?> ban(Integer id);

    Result<?> update(User user);
    Result<?> delete(User user);
    Result<?> findById(long id);
    Result<?> findAll();
    Result<?> findByEmail(String email);
    Result<?> findByUsername(String username);
    Result<?> register(RegisterVo u);
    Result<?> login(LoginVo user) throws Exception;
    Result<?> logicDel(Long id);
    Result<?> updatePw(String token,String newPw) throws Exception;
    Result<?> logout(String token);
}
