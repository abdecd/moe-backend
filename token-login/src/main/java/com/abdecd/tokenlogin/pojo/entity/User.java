package com.abdecd.tokenlogin.pojo.entity;

import com.abdecd.tokenlogin.common.dataencrypt.EncryptStrHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id; // 必须
    private String password; // 必须
    private String permission; // 必须
    private Byte status; // 必须

    private String nickname;
    @TableField(typeHandler = EncryptStrHandler.class)
    private String email;
    private LocalDateTime createTime;

    public static User ofEmpty() {
        return new User()
                .setId(null)
                .setPassword("")
                .setPermission("")
                .setStatus((byte) 0)
                .setNickname("")
                .setEmail("")
                .setCreateTime(LocalDateTime.now());
    }
}
