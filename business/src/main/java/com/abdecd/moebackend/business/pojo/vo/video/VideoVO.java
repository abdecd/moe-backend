package com.abdecd.moebackend.business.pojo.vo.video;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoVO {
    Long id;
    Long videoGroupId;
    Integer index;
    String title;
    String description;
    String cover;
    String link;
    LocalDateTime uploadTime;
}
