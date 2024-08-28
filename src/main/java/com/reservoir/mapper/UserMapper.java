package com.reservoir.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.reservoir.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // Custom methods for User entity
}
