package com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup;

import lombok.Data;

@Data
public class VideoCompleteVO {
    private Long id;
    private Long videoGroupId;
    private Integer index;
    private String title;
    private String description;
    private String cover;
    private String link;
}
