package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupAddDTO;
import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.backstage.bangumiVideoGroup.BangumiVideoGroupVO;

public interface BangumiVideoGroupService {
    void deleteByVid(Long id);

    void insert(BangumiVideoGroup bangumiVideoGroup);

    BangumiVideoGroupVO getByVid(Long vid);

    Long insert(BangumiVideoGroupAddDTO bangumiVideoGroupAddDTO);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    BangumiVideoGroupVO getByVideoId(Long videoGroupId);
}
