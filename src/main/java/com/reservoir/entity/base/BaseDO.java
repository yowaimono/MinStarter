package com.reservoir.entity.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDO implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
