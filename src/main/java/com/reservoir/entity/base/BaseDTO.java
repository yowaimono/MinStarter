package com.reservoir.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDTO implements Serializable {
    @ApiModelProperty(value = "业务主键")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后编辑时间")
    private Date updateTime;
}
