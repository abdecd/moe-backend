package com.abdecd.moebackend.business.pojo.vo.favorite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BangumiVideoGroupFavoriteVO extends FavoriteVO {
    private String latestVideoTitle;
    private String lastWatchVideoTitle;
    private Long lastWatchVideoId;
    private Integer lastWatchVideoIndex;
}
