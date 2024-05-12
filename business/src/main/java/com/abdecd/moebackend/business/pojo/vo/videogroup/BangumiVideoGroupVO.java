package com.abdecd.moebackend.business.pojo.vo.videogroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BangumiVideoGroupVO extends VideoGroupVO {
    private String releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
}
