package com.abdecd.moebackend.business.pojo.vo.plainuser;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllVO {
    private Long id;
    private String permission;
    private Integer status;
    private String nickname;
    private String email;
    private LocalDateTime createTime;
    private String avatar;
    private String signature;
}
