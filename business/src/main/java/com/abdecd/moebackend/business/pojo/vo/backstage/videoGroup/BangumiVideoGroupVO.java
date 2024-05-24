package com.abdecd.moebackend.business.pojo.vo.backstage.videoGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BangumiVideoGroupVO extends VideoGroupVO {
    private LocalDateTime releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
    private LocalDateTime updateTime;
}
