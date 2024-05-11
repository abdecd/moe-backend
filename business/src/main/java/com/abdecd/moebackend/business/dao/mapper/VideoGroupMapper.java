package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.ArrayList;

@Mapper
public interface VideoGroupMapper extends BaseMapper<VideoGroup> {
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=long.class)
    @Insert("INSERT INTO video_group (user_id, title, description, cover, create_time, type, weight) VALUES(#{userId}, #{title}, #{description}, #{cover}, #{create_time}, #{type}, #{weight})")
    int insertVideoGroup(VideoGroup videoGroup);

    void update(VideoGroup videoGroup);

    @Select("SELECT * FROM video_group  LIMIT #{pageSize} OFFSET #{offset}")
    ArrayList<VideoGroup> selectbyPage(Integer offset, Integer pageSize);
}
