package com.abdecd.moebackend.business.pojo.vo.report;

import com.abdecd.moebackend.business.dao.entity.UserComment;
import com.abdecd.moebackend.business.pojo.vo.comment.UserCommentVO;
import com.abdecd.moebackend.business.pojo.vo.comment.UserCommentVOBasic;
import com.abdecd.moebackend.business.pojo.vo.plainuser.UploaderVO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ReportCommentVO {
    private Integer id;
    private UploaderVO userDetail;
    private UserCommentVO comment;
    private String reason;
    private String createTime;
    private Integer status;
}
