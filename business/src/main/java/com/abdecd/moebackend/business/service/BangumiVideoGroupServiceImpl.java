package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.bangumiVideoGroup.BangumiVideoGroupVO;

public interface BangumiVideoGroupServiceImpl {
    void deleteByVid(Long id);

    void insert(BangumiVideoGroup bangumiVideoGroup);

    BangumiVideoGroupVO getByVid(Long vid);

    Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    BangumiVideoGroupVO getByVideoId(Long videoGroupId);
}
