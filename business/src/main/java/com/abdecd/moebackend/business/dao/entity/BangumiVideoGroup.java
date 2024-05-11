package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BangumiVideoGroup {
    private Long videoGroupId;
    private String releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
}
