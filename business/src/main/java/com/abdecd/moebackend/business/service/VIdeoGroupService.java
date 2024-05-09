package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoVo;

import java.util.ArrayList;

public interface VIdeoGroupService {
    Long insert(VIdeoGroupDTO videoGroupDTO);

    void delete(Long id);

    void update(VIdeoGroupDTO videoGroupDTO);

    VideoGroupVO getById(Long id);

    ArrayList<VideoVo> getContentById(Long id);

    Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    BangumiVideoGroupVO getByVideoId(Long videoGroupId);
}
