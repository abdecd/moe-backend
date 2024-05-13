package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Accessors(chain = true)
@Data
public class BangumiVideoGroupTimeScheduleVO {
    @JsonUnwrapped
    VideoGroupWithDataVO videoGroupWithDataVO;
    LocalTime willUpdateTime;
    String willUpdateTitle;
}
