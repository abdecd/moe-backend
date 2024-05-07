package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.session.ResultHandler;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface VIdeoGroupMapper extends BaseMapper<VideoGroup> {
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=long.class)
    @Insert("INSERT INTO video_group (user_id, title, description, cover, create_time, type, weight) VALUES(#{user_id}, #{title}, #{description}, #{cover}, #{create_time}, #{type}, #{weight})")
    int insertVideoGroup(VideoGroup videoGroup);

    void update(VideoGroup videoGroup);
}
