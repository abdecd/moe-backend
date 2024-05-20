package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.PlainUserHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlainUserHistoryMapper extends BaseMapper<PlainUserHistory> {
    @Select("SELECT COUNT(id) FROM plain_user_history WHERE video_group_id = #{videoGroupId}")
    Integer getWatchCnt(Long videoGroupId);
}
