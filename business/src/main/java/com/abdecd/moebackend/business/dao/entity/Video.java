package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Video {
    private Long id;
    private Long video_group_id;
    private Integer index;
    private String title;
    private String cover;
    private String description;
    private String link;
    private String upload_time;
    private Integer status;
}
