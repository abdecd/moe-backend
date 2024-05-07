package com.abdecd.moebackend.business.pojo.vo.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCommentVO {
    private Long id;
    private UserCommentVOBasic.UserDetail userDetail;
    private Long toId;
    private UserCommentVOBasic.UserDetail toUserDetail;
    private String content;
    private LocalDateTime timestamp;
}
