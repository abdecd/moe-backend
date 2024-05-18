package com.abdecd.moebackend.business.pojo.vo.plainuser;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class HistoryVO {
    private UploaderVO uploader;
    private Long videoGroupId;
    private Byte videoGroupType;
    private String videoGroupTitle;
    private String videoGroupCover;
    private Long videoId;
    private Integer videoIndex;
    private String videoTitle;
    private LocalDateTime timestamp;
}
