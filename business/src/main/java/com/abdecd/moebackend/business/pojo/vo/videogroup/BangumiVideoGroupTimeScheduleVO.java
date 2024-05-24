package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class BangumiVideoGroupTimeScheduleVO {
    @JsonUnwrapped
    VideoGroupWithDataVO videoGroupWithDataVO;
    LocalDateTime willUpdateTime;
    Integer willUpdateIndex;
    String willUpdateTitle;
}
