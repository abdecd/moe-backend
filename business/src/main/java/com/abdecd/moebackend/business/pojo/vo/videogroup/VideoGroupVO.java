package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoGroupVO {
    private Long id;
    private String title;
    private UploaderVO uploader;
    private String cover;
    private String description;
    private List<VideoGroupTag> tags;
    private Byte type;
    private LocalDateTime createTime;
}
