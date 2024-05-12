package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
    private Integer type;
    private Double weight;
}
