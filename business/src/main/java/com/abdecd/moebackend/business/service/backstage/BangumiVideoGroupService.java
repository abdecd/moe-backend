package com.abdecd.moebackend.business.service.backstage;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup.BangumiVideoGroupVO;

import java.util.ArrayList;

public interface BangumiVideoGroupService {
    /**
     * 根据id删除视频组
     */
    void deleteByVid(Long id);

    /**
     * 添加视频组
     */
    void insert(BangumiVideoGroup bangumiVideoGroup);

    /**
     * 根据id获取视频组
     */
    BangumiVideoGroupVO getByVid(Long vid);

    /**
     * 添加视频组，并返回新增视频组id
     */
    Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO);

    /**
     * 更新视频组
     */
    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    /**
     * 根据id获取视频组
     */
    BangumiVideoGroupVO getByVideoId(Long videoGroupId);


    ArrayList<com.abdecd.moebackend.business.pojo.vo.backstage.videoGroup.BangumiVideoGroupVO> getBangumiVideoGroupList(Integer pageIndex, Integer pageSize, String id, String title, Byte status);

    Integer getBangumiVideoGroupListCount(String id, String title, Byte status);
}
