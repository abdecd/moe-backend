package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VideoGroupAndTag {
    private Long id;
    private Long tag_id;
    private Long video_group_id;
}
