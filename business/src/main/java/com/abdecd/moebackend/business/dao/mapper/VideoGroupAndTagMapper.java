package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.VideoGroupAndTag;
import com.abdecd.moebackend.business.dao.entity.VideoGroupTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface VideoGroupAndTagMapper extends BaseMapper<VideoGroupAndTag> {
    @Select("SELECT tag_id FROM video_group_and_tag where video_group_id = #{id}")
    ArrayList<Long> selectByVid(Long id);

    @Delete("DELETE FROM video_group_and_tag WHERE video_group_id = #{id}")
    void deleteByVideoGroupId(Long id);
}
