package com.transaction_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user_point")
public class UserPoint {
    @TableId(value = "user_id", type = IdType.INPUT)
    private Integer userId;

    @TableField(value = "point")
    private Integer point;
}