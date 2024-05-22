package com.abdecd.moebackend.business.pojo.vo.plainuser;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllVO {
    private Long id;
    private String nickname;
    private String email;
    private Integer status;
    private String permission;
    private LocalDateTime createTime;
}
