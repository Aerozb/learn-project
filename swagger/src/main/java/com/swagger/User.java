package com.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("用户实体类")
public class User
{
    @ApiModelProperty("姓名")
    String name;
    @ApiModelProperty("年龄")
    Integer age;
    @ApiModelProperty("主键id")
    Integer id;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改时间")
    private Date updateTime;
    @ApiModelProperty("版本，用于乐观锁")
    Integer version;
    @ApiModelProperty("逻辑删除字段")
    private Integer deleted;
}