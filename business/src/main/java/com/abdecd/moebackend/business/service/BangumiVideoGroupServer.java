package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;

public interface BangumiVideoGroupServer {
    void deleteByVid(Long id);

    void insert(BangumiVideoGroup bangumiVideoGroup);

    BangumiVideoGroupVO getByVid(BangumiVideoGroupVO bangumiVideoGroupVO);

    Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    BangumiVideoGroupVO getByVideoId(Long videoGroupId);
}
