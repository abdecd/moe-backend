package com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.pojo.vo.common.UploaderVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@Accessors(chain = true)
public class VideoGroupVO {
    private Long videoGroupId;
    private String title;
    private String description;
    private String cover;
    private UploaderVO uploader;
    private ArrayList<VideoGroupTag> tags;
    private Integer type;
}

