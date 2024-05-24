package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.pojo.vo.backstage.videoGroup.PlainVideoGroupVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface PlainVideoGroupMapper {
    ArrayList<PlainVideoGroupVO> selectVideoGroupList(
            Integer pageIndex,
            Integer pageSize,
            String id,
            String title,
            Byte status
    );

    Integer selectVideoGroupListCount(String id, String title, Byte status);
}
