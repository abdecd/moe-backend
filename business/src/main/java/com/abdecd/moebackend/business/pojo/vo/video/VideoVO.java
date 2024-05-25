package com.abdecd.moebackend.business.pojo.vo.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Accessors(chain = true)
@Data
public class VideoVO {
    Long id;
    Long videoGroupId;
    Integer index;
    String title;
    String description;
    String cover;
    ArrayList<VideoSrcVO> src;
    LocalDateTime uploadTime;
    @JsonIgnore
    Byte status;
    @JsonIgnore
    String bvid;
    @JsonIgnore
    String epid;
}
