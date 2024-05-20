package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@TableName("danmaku")
public class Danmaku {
    @TableId(type = IdType.AUTO)
    Long id;
    Long videoId;
    Long userId;
    LocalDateTime timestamp;
    Integer begin;
    Integer mode;
    Integer size;
    String color;
    Long time;
    Integer pool;
    String author;
    String text;
}
