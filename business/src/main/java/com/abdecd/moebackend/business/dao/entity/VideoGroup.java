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
@TableName("video_group")
public class VideoGroup {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String cover;
    private String description;
    private LocalDateTime createTime;
    private Byte type;
    private Double weight;
    private String tags;
    @TableField(value = "status")
    private Byte videoGroupStatus;// 与 bangumiVideoGroup 重复，所以改一下名字

    public static class Type {
        public static final Byte PLAIN_VIDEO_GROUP = 0;
        public static final Byte ANIME_VIDEO_GROUP = 1;
    }

    public static class Status {
        public static final Byte TRANSFORMING = 2;
        public static final Byte ENABLE = 1;
        public static final Byte DISABLE = 0;
    }
}
