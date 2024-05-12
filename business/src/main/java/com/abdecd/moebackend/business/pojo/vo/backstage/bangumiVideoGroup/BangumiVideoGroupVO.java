package com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.pojo.vo.common.UploaderVO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BangumiVideoGroupVO{
    private Long videoGroupId;
    private String title;
    private UploaderVO uploader;
    private String cover;
    private String description;
    private Integer type;
    private String releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
    private ArrayList<VideoGroupTag> tags;
}