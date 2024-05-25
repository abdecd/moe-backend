package com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@Accessors(chain = true)
public class VideoGroupVO {
    private Long id;
    private String title;
    private String description;
    private String cover;
    private UploaderVO uploader;
    private String tags;
    private Byte type;
    private String createTime;
    private Integer watchCnt;
    private Integer likeCnt;
    private Integer favoriteCnt;
    private Boolean userLike;
    private Boolean userFavorite;
    private Integer commentCnt;
    private Integer danmakuCnt;
    private Double weight;
}

