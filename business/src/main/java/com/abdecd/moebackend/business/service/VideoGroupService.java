package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupListVO;
import com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup.VideoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.backstage.commonVideoGroup.VideoVo;

import java.util.ArrayList;

public interface VideoGroupService {
    Long insert(VideoGroupDTO videoGroupDTO);

    void delete(Long id);

    void update(VideoGroupDTO videoGroupDTO);

    VideoGroupVO getById(Long id);

    ArrayList<VideoVo> getContentById(Long id);

    Integer getTypeByVideoId(Long vid);

    VideoGroupListVO getVideoGroupList(Integer page, Integer pageSize);
}
