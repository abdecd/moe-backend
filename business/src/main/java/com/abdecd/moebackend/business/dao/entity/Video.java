package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@TableName("video")
public class Video {
    @TableId(type = IdType.AUTO)
    Long id;
    Long videoGroupId;
    @TableField(value = "`index`")
    Integer index;
    String title;
    String description;
    String cover;
    LocalDateTime uploadTime;
    Byte status;

    public static class Status {
        public static final Byte ENABLE = 1;
        public static final Byte TRANSFORMING = 2;
        public static final Byte PRELOAD = 3;
    }
}
