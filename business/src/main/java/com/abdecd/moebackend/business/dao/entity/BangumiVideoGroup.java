package com.abdecd.moebackend.business.dao.entity;

import com.abdecd.moebackend.business.pojo.vo.videogroup.BangumiVideoGroupVO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Data
@TableName("bangumi_video_group")
public class BangumiVideoGroup {
    private Long videoGroupId;
    private LocalDateTime releaseTime;
    private String updateAtAnnouncement;
    private Integer status;
    private  LocalDateTime updateTime;

    public static class Status {
        static final Integer FINISHED = 0;
        static final Integer LOADING = 1;
    }
}
