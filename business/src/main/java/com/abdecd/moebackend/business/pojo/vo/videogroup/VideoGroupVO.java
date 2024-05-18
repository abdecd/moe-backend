package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoGroupVO {
    private Long id;
    private String title;
    private UploaderVO uploader;
    private String cover;
    private String description;
    private String tags;
    private Byte type;
    private LocalDateTime createTime;
    private Byte videoGroupStatus;
}
