package com.abdecd.moebackend.business.pojo.vo.favorite;

import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupVO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class FavoriteVO {
    @JsonUnwrapped
    private VideoGroupVO videoGroupVO;
}
