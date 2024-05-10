package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;

@Data
public class BangumiVideoGroup {
    private Long videoGroupId;
    private String releaseTime;
    private String updateAtAnnouncement;
    private Integer status;

    public Long getVideo_group_id() {
        return videoGroupId;
    }
}
