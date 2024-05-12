package com.abdecd.moebackend.business.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class VideoGroupAndTag {
    private Long id;
    private Long tagId;
    private Long videoGroupId;
}
