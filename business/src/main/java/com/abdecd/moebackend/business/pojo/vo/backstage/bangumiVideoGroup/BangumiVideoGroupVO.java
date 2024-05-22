package com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup;

import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import lombok.Data;

@Data
public class BangumiVideoGroupVO{
    private String id;
    private Long videoGroupId;
    private String title;
    private UploaderVO uploader;
    private String cover;
    private String description;
    private Integer type;
    private String releaseTime;
    private String createTime;
    private String updateAtAnnouncement;
    private Integer status;
    private String tags;
    private Integer watchCnt;
    private Integer likeCnt;
    private Integer favoriteCnt;
    private Boolean userLike;
    private Boolean userFavorite;
    private Integer commentCnt;
    private Integer danmakuCnt;
}
