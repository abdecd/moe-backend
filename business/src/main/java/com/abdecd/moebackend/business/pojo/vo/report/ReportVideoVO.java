package com.abdecd.moebackend.business.pojo.vo.report;

import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ReportVideoVO {
    private Integer id;
    private UploaderVO user;
    private VideoVO video;
    private String reason;
    private String createTime;
    private Integer status;
}
