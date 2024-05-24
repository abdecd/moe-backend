package com.abdecd.moebackend.business.pojo.vo.videogroup;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class BangumiTimeTableBackVO {
    private Long id;
    private Long videoGroupId;
    private Long videoId;
    private LocalDateTime updateTime;
    private Byte status;
    private String videoGroupTitle;
    private Integer videoIndex;
    private String videoTitle;
}
