package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Video {
    private Long id;
    private Long videoGroupId;
    private Integer index;
    private String title;
    private String cover;
    private String description;
    private String link;
    private String uploadTime;
    private Integer status;
}
