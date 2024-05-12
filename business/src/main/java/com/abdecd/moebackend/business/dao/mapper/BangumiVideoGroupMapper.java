package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BangumiVideoGroupMapper extends BaseMapper<BangumiVideoGroup> {
    @Delete("DELETE FROM bangumi_video_group WHERE video_group_id = #{vid}")
    void deleteByVid(Long vid);

    void update(BangumiVideoGroup bangumiVideoGroup);

    @Select("SELECT * FROM bangumi_video_group WHERE video_group_id = #{videoGroupId}")
    BangumiVideoGroup selectByVid(Long videoGroupId);
}
