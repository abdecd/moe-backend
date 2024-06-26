package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_comment")
public class UserComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long videoId;
    private Long userId;
    private Long toId;
    private String content;
    private LocalDateTime timestamp;
    private Byte status;

    public static class Status {
        public static final Byte ENABLE = 1;
        public static final Byte DELETED = 0;
    }
}
