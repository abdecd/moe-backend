package com.abdecd.moebackend.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("plain_user_favorite")
public class PlainUserFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long videoGroupId;
}
