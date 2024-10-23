package com.reservoir.mapper;

import com.reservoir.entity.admin.SystemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    // Custom methods for SystemUser entity
}
