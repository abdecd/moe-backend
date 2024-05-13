package com.abdecd.moebackend.business.pojo.vo.videogroup;


import com.abdecd.moebackend.business.pojo.vo.statistic.StatisticDataVO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VideoGroupBigVO {
    @JsonUnwrapped
    private VideoGroupVO videoGroupVO;
    Object contents; // todo 待重构
    @JsonUnwrapped
    private StatisticDataVO statisticDataVO;
    String bvid;
    String epid;
}
