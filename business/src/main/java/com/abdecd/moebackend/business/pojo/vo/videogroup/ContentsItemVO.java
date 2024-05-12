package com.abdecd.moebackend.business.pojo.vo.videogroup;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ContentsItemVO {
    private Long videoId;
    private Long videoGroupId;
    private Integer index;
    private String title;
    private String videoCover;
}
