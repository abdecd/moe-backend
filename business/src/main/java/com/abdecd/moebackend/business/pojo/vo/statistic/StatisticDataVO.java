package com.abdecd.moebackend.business.pojo.vo.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class StatisticDataVO {
    private Long watchCnt;
    private Long likeCnt;
    private Long favoriteCnt;
    private Boolean userLike;
    private Boolean userFavorite;
    private Long commentCnt;
    private Long danmakuCnt;
}
