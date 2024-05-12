package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class BangumiVideoGroup {
    private Long videoGroupId;
    private LocalDateTime releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
}
