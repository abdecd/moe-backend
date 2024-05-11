package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PlainUserTotalWatchTime {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long videoGroupId;
    private Long videoId;
    private Integer index;
    private Long totalWatchTime;
}
