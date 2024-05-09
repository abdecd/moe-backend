package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.pojo.vo.common.VideoCompleteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    @Select("SELECT * FROM video WHERE video_group_id = #{id}")
    ArrayList<Video> getByGroupid(Long id);

    @Select(("SELECT * FROM video WHERE video_group_id = #{videoGroupId}"))
    ArrayList<Video> selectByGid(Integer videoGroupId);
}
