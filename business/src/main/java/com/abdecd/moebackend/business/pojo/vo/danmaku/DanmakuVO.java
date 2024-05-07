package com.abdecd.moebackend.business.pojo.vo.danmaku;

import lombok.Data;

@Data
public class DanmakuVO {
    Long id;
    Double begin;
    Integer mode;
    Integer size;
    String color;
    Long time;
    Integer pool;
    String author;
    String text;
}
