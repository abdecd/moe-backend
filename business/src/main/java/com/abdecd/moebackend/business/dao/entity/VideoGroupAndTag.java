package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("video_group_and_tag")
public class VideoGroupAndTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tagId;
    private Long videoGroupId;
}
