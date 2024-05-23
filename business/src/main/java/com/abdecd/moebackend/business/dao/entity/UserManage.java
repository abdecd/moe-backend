package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserManage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String password;
    private String permission;
    private Integer status;
    private String nickname;
    private String email;
    private LocalDateTime createTime;
}
