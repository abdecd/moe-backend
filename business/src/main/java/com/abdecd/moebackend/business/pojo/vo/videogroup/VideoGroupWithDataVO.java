package com.abdecd.moebackend.business.pojo.vo.videogroup;

import com.abdecd.moebackend.business.pojo.vo.statistic.StatisticDataVO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VideoGroupWithDataVO {
    @JsonUnwrapped
    private VideoGroupVO videoGroupVO;
    @JsonUnwrapped
    private StatisticDataVO statisticDataVO;
}
